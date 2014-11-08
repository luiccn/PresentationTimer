package ciafrino.presentationtimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Presentations extends Activity {

	private PresentationsListAdapter adapter;
    private Presentation itemToRemove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		
		setupListViewAdapter();
		
		setupAddPaymentButton();
	}

    public void editPresentationOnClickHandler(View v) {
        Toast toast = Toast.makeText(this,"Clicked Edit",Toast.LENGTH_SHORT);
        toast.show();
    }

	public void removePresentationOnClickHandler(View v) {

        itemToRemove = (Presentation)v.getTag();

        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
		adapter = new PresentationsListAdapter(Presentations.this, R.layout.atom_pay_list_item, new ArrayList<Presentation>());
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
