package ciafrino.presentationtimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Presentations extends Activity {

	private PresentationsListAdapter adapter;
    private Presentation itemToRemove;
    private PresentationDatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
        databaseHelper = new PresentationDatabaseHelper(this);

        setupListViewAdapter();
		
		setupAddPaymentButton();
	}

    public void editPresentationOnClickHandler(View v) {
        Toast toast = Toast.makeText(this,"Clicked Edit",Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, CreateEditPresentation.class);
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
//        databaseHelper.insertPresentation("PresentationName1","stepName",0,1000,"Annotation1",0);
//        databaseHelper.insertPresentation("PresentationName2","stepName2",0,1000,"Annotation2",0);
        Cursor cursor = databaseHelper.getAllData();
        cursor.moveToFirst();
        list.add(new Presentation(cursor.getString(1), cursor.getInt(0)));
        while(cursor.moveToNext()) {
            list.add(new Presentation(cursor.getString(1), cursor.getInt(0)));
        }
        adapter = new PresentationsListAdapter(Presentations.this, R.layout.atom_pay_list_item,list);
		ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
	}
	
	private void setupAddPaymentButton() {
		findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				adapter.insert(new Presentation("", 0), 0);
			}
		});
	}
}
