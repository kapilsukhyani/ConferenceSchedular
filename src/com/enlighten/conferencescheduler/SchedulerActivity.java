package com.enlighten.conferencescheduler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.enlighten.conferenceschedular.R;
import com.enlighten.conferencescheduler.SchedulerFactory.SchedulerType;
import com.enlighten.conferencescheduler.TrackRule.SessionRule;
import com.enlighten.conferencescheduler.models.Conference;
import com.enlighten.conferencescheduler.models.Talk;
import com.enlighten.conferencescheduler.utils.Utils;

public class SchedulerActivity extends ListActivity implements OnClickListener {

	private TrackRule trackRule;
	private Spinner unitSpinner;
	private EditText talkTitleText, talkDurationText;
	private Button addTalkButton, scheduleTalksButton;
	private List<Talk> addedTalks = new ArrayList<Talk>();

	/**
	 * Loads default track rules in background and notifies on main thread
	 */
	private AsyncTask<Void, Void, TrackRule> defaultTrackRuleLoader = new AsyncTask<Void, Void, TrackRule>() {
		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = Utils.showWaitProgressDialog(
					getString(R.string.track_rule_loading),
					SchedulerActivity.this);
		};

		@Override
		protected TrackRule doInBackground(Void... params) {
			String rulesJson = Utils.streamToString(getResources()
					.openRawResource(R.raw.track_rule));

			try {

				JSONObject trackRuleJsonObject = new JSONObject(rulesJson);
				JSONArray sessionsJsonArray = trackRuleJsonObject
						.getJSONArray("sessions");
				List<SessionRule> sessionRules = new ArrayList<TrackRule.SessionRule>();
				SessionRule sessionRule;
				JSONObject sessionRuleJsonObject;
				for (int i = 0; i < sessionsJsonArray.length(); i++) {
					sessionRuleJsonObject = sessionsJsonArray.getJSONObject(i);

					sessionRule = new SessionRule(
							sessionRuleJsonObject.getDouble("startTime"),
							sessionRuleJsonObject.getDouble("minEndTime"),
							sessionRuleJsonObject.getDouble("maxEndTime"),
							sessionRuleJsonObject.getBoolean("break"));
					sessionRules.add(sessionRule);

				}

				TrackRule trackRule = new TrackRule(sessionRules,
						trackRuleJsonObject.getDouble("trackStartTime"),
						trackRuleJsonObject.getDouble("trackMaxDuration"),
						trackRuleJsonObject.getDouble("maxTalkDuration"),
						trackRuleJsonObject.getInt("noOfBreaks"));

				return trackRule;

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(TrackRule result) {
			dialog.dismiss();
			if (null == result) {
				Utils.showSingleButtonAutoDissmissAlert(
						getString(R.string.wait_tile), getString(R.string.ok),
						SchedulerActivity.this);
			} else {

				SchedulerActivity.this.trackRule = result;
			}

		};
	};

	/**
	 * Schedules talks in background and then opens a new screen to show
	 * scheduled conference
	 */
	private class SchedulerTask extends AsyncTask<Void, Void, Conference> {

		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = Utils.showWaitProgressDialog("Scheduling added talks",
					SchedulerActivity.this);
		};

		protected void onPostExecute(Conference result) {
			dialog.dismiss();
			((ConferenceSchedulerApplication) getApplication())
					.setConference(result);
			Intent conferenceIntent = new Intent(SchedulerActivity.this,
					ConferenceActivity.class);
			startActivity(conferenceIntent);

			Log.d("ConferenceScheduler",
					"Schdeuled conference: " + result.toString());
		};

		@Override
		protected Conference doInBackground(Void... params) {

			Conference conference = SchedulerFactory
					.getFactory()
					.getScheduler(SchedulerType.CombinationScheduler, trackRule)
					.scheduleTalks(addedTalks);

			return conference;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		// I may pass a path to different track rule file in future if multiple
		// track rules are supported
		defaultTrackRuleLoader.execute((Void) null);
		initView();
	}

	/**
	 * Initializes views and their event callbacks
	 */
	private void initView() {
		unitSpinner = (Spinner) findViewById(R.id.duration_unit);
		talkTitleText = (EditText) findViewById(R.id.talk_title);
		talkDurationText = (EditText) findViewById(R.id.talk_duration);
		addTalkButton = (Button) findViewById(R.id.add_talk);
		addTalkButton.setOnClickListener(this);
		scheduleTalksButton = (Button) findViewById(R.id.schedule_talks);
		scheduleTalksButton.setOnClickListener(this);
		getListView().setAdapter(new TalkListAdapter(addedTalks));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.add_talk:
			addTalkToList();
			break;

		case R.id.schedule_talks:
			scheduleTalks();
			break;

		default:
			break;
		}
	}

	/**
	 * Starts scheduling
	 */
	private void scheduleTalks() {
		if (null != trackRule) {
			if (addedTalks.isEmpty()) {
				Utils.showSingleButtonAutoDissmissAlert("No talk is added yet",
						getString(R.string.ok), SchedulerActivity.this);
			} else {
				new SchedulerTask().execute((Void) null);
			}
		} else {
			Utils.showSingleButtonAutoDissmissAlert(
					"Track rules has not been initialized, can not schedule talks",
					getString(R.string.ok), SchedulerActivity.this);
		}
	}

	/**
	 * Adds a talk to list
	 */
	private void addTalkToList() {

		String talkTitle = talkTitleText.getText().toString();
		String talkDuration = talkDurationText.getText().toString();
		String unit = (String) unitSpinner.getSelectedItem();

		if (TextUtils.isEmpty(talkTitle)) {
			talkTitleText.setError("Talk title canot be empty");
			return;
		} else if (TextUtils.isEmpty(talkDuration)) {
			talkDurationText.setError("Talk duration cannot be empty");
			return;
		} else if (unit == null) {
			Utils.showSingleButtonAutoDissmissAlert("Unit has to be selected",
					getString(R.string.ok), SchedulerActivity.this);
			return;
		} else {
			try {
				int talkDurationInt = Integer.valueOf(talkDuration);

				// convert talk duration to mins if lightning as a unit is
				// selected
				if (unit.equalsIgnoreCase(getResources().getStringArray(
						R.array.units)[1])) {
					talkDurationInt *= Constants.LIGHTNING_TO_MINUTE_MULTIPLIER;
				}

				// Talk duration cannot be more that max session duration in a
				// track
				if (((double) talkDurationInt / 60) > trackRule
						.getMaxTalkDuration()) {
					talkDurationText.setError("Max talk duration is "
							+ trackRule.getMaxTalkDuration() * 60 + "mins");
				} else {
					Talk talk = new Talk(talkDurationInt, talkTitle);
					addedTalks.add(talk);
					((TalkListAdapter) getListView().getAdapter())
							.notifyDataSetChanged();
					resetTalkParamViews();
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
				Utils.showSingleButtonAutoDissmissAlert(
						"Talk duration is supported as a whole number as of now",
						getString(R.string.ok), SchedulerActivity.this);
				return;
			}

		}

	}

	private void resetTalkParamViews() {
		talkDurationText.setText("");
		talkTitleText.setText("");
	}

	/**
	 * Represents data adapter for talks list being shown to the user
	 * 
	 * @author kapil
	 * 
	 */
	private class TalkListAdapter extends BaseAdapter {
		private List<Talk> talks;

		public TalkListAdapter(List<Talk> talks) {
			this.talks = talks;
		}

		@Override
		public int getCount() {
			return talks.size();
		}

		@Override
		public Object getItem(int position) {
			return talks.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						android.R.layout.simple_list_item_2, null);
				((TextView) convertView.findViewById(android.R.id.text1))
						.setTextColor(getResources().getColor(
								android.R.color.black));
				((TextView) convertView.findViewById(android.R.id.text2))
						.setTextColor(getResources().getColor(
								android.R.color.black));
			}

			Talk talk = (Talk) getItem(position);
			TextView talkTitleView = (TextView) convertView
					.findViewById(android.R.id.text1);
			TextView talkDurationView = (TextView) convertView
					.findViewById(android.R.id.text2);

			talkTitleView.setText(talk.getTalkTitle());
			talkDurationView.setText(talk.getTalkDurationInMins() + " Mins");
			return convertView;
		}

	}

}
