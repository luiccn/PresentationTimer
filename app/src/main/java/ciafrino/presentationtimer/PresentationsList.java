package ciafrino.presentationtimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PresentationsList extends Activity {

	private PresentationsListAdapter adapter;
    private Presentation item;
    private PresentationDatabaseHelper databaseHelper;
    EditText new_presentation_name;
    Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
        databaseHelper = PresentationDatabaseHelper.getDatabaseHelper(this);

        setupListViewAdapter();
		
		newPresentationButtonHandler();
	}

    public void editPresentationOnClickHandler(View v) {

        item = (Presentation)v.getTag();
        Intent intent = new Intent(this, CreateEditPresentation.class);
        intent.putExtra("presentation_name", item.getName());
        intent.putExtra("presentation_id", item.getId());

        startActivity(intent);
    }

    public void PresentationScreenOnClickHandler(View v) {

        item = (Presentation)v.getTag();
        Intent intent = new Intent(this, PresentationScreen.class);
        intent.putExtra("presentation_id", item.getId());
        startActivity(intent);
    }


	public void removePresentationOnClickHandler(View v) {

        item = (Presentation)v.getTag();


        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deletePresentation(String.valueOf(item.getId()));
                        adapter.remove(item);
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

            while (cursor.moveToNext()) {
                list.add(new Presentation(cursor.getString(1), cursor.getInt(0)));
            }
        }
        adapter = new PresentationsListAdapter(PresentationsList.this, R.layout.atom_pay_list_item,list);

		ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
        ((Values) getApplication()).setPresentations_list(list);
	}
	
	private void newPresentationButtonHandler() {
        new_presentation_name = (EditText) findViewById(R.id.new_presentation_name);

            c = this;

            findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String text =  ((EditText) findViewById(R.id.new_presentation_name)).getText().toString();
                    if(!text.trim().equals("")) {
                        Presentation presentation = new Presentation(new_presentation_name.getText().toString(), 0);
                        presentation.setId(databaseHelper.insertPresentation(presentation));
                        if (presentation.getId() == -1) throw new IllegalStateException();

                        Intent intent = new Intent(c, CreateEditPresentation.class);
                        intent.putExtra("presentation_id", presentation.getId());
                        startActivity(intent);
                        adapter.insert(presentation, 0);
                    }else{
                        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
                        alertDialog.setTitle("No Name for Presentation");
                        alertDialog.setMessage("Please give a name to your presentation");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Do Nothing
                            }
                        });
                        alertDialog.show();
                    }

                }
            });
	}
}
