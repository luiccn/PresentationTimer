package ciafrino.presentationtimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PresentationsList extends Activity {

	private PresentationsListAdapter adapter;
    private Presentation itemToRemove;
    private PresentationDatabaseHelper databaseHelper;
    EditText new_presentation_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
        databaseHelper = new PresentationDatabaseHelper(this);

        setupListViewAdapter();
		
		newPresentationButtonHandler();
	}

    public void editPresentationOnClickHandler(View v) {
        TextView text = (TextView) findViewById(R.id.atomPay_name);
        Toast toast = Toast.makeText(this,text.getText().toString(),Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(this, CreateEditPresentation.class);
        intent.putExtra("presentation_name", text.getText().toString());
        startActivity(intent);
    }

    public void PresentationScreenOnClickHandler(View v) {
        Toast toast = Toast.makeText(this,"Clicked Presentation",Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, PresentationScreen.class);
        startActivity(intent);
    }


	public void removePresentationOnClickHandler(View v) {

        itemToRemove = (Presentation)v.getTag();


        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deletePresentation(String.valueOf(itemToRemove.getId()));
                        adapter.remove(itemToRemove);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
	}

	private void setupListViewAdapter() {
        ArrayList<Presentation> list = new ArrayList<Presentation>();

        Cursor cursor = databaseHelper.getPresentationList();
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            list.add(new Presentation(cursor.getString(1), cursor.getInt(0)));
            while (cursor.moveToNext()) {
                System.out.println("Index:" + cursor.getColumnIndex("id"));

                list.add(new Presentation(cursor.getInt(0) + cursor.getString(1), cursor.getInt(0)));

            }
        }
        adapter = new PresentationsListAdapter(PresentationsList.this, R.layout.atom_pay_list_item,list);
		ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
	}
	
	private void newPresentationButtonHandler() {
        new_presentation_name = (EditText) findViewById(R.id.new_presentation_name);


		findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

                Presentation presentation = new Presentation(new_presentation_name.getText().toString(), 0);
                presentation.setId(databaseHelper.insertPresentation(presentation));
                if(presentation.getId() == -1) throw new IllegalStateException();

				adapter.insert(presentation, 0);
                ((Values) getApplication()).setPresentations_list(presentation);

			}
		});
	}
}
