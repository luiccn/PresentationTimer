package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class StepListAdapter extends ArrayAdapter<Step> {

	protected static final String LOG_TAG = StepListAdapter.class.getSimpleName();

	private List<Step> items;
	private int layoutResourceId;
	private Context context;

	public StepListAdapter(Context context, int layoutResourceId, List<Step> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StepHolder holder;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new StepHolder();
		holder.step = items.get(position);
		holder.removeStepButton = (ImageButton)row.findViewById(R.id.remove_step);
        holder.removeStepButton.setTag(holder.step);

		holder.name = (TextView)row.findViewById(R.id.create_step_text);
        holder.name.setTag(holder.step);

		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(StepHolder holder) {
		holder.name.setText(holder.step.getName());
	}

	public static class StepHolder {
		Step step;
		TextView name;
		ImageButton removeStepButton;
	}

}
