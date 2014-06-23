package com.enlighten.conferencescheduler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.enlighten.conferenceschedular.R;
import com.enlighten.conferencescheduler.models.Conference;
import com.enlighten.conferencescheduler.models.Session;
import com.enlighten.conferencescheduler.models.Talk;
import com.enlighten.conferencescheduler.models.Track;

@SuppressLint("ValidFragment")
public class ConferenceActivity extends FragmentActivity {

	private Conference conference;
	private ViewPager trackPager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_conference);
		conference = ((ConferenceSchedulerApplication) getApplication())
				.getConference();
		trackPager = (ViewPager) findViewById(R.id.track_veiw_pager);
		trackPager.setAdapter(new TrackAdapter(getSupportFragmentManager()));
	}

	public static class TrackFragment extends Fragment {

		private Track track;
		int id;
		private TextView trackTitleText;
		private ExpandableListView sessionListView;

		public TrackFragment(Track track, int id) {
			this.track = track;
			this.id = id;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup root = (ViewGroup) inflater.inflate(
					R.layout.fragment_track, null);
			initView(root);
			return root;
		}

		private void initView(ViewGroup root) {
			trackTitleText = (TextView) root.findViewById(R.id.track_title);
			sessionListView = (ExpandableListView) root
					.findViewById(R.id.session_list);

			trackTitleText.setText("Track_" + id);

			sessionListView.setAdapter(new SessionListAdapter(track,
					getActivity()));
		}

		private static class SessionListAdapter extends
				BaseExpandableListAdapter {

			private Track track;
			private Context context;
			private int blackColor;

			public SessionListAdapter(Track track, Context context) {
				this.track = track;
				this.context = context;
				blackColor = context.getResources().getColor(
						android.R.color.black);
			}

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				return track.getSessions().get(groupPosition).getTalks()
						.get(childPosition);
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return groupPosition * childPosition;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = ((LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							.inflate(
									android.R.layout.simple_expandable_list_item_2,
									null);
					((TextView) convertView.findViewById(android.R.id.text1))
							.setTextColor(blackColor);
					((TextView) convertView.findViewById(android.R.id.text2))
							.setTextColor(blackColor);
				}
				Talk talk = (Talk) getChild(groupPosition, childPosition);

				TextView talkTitle = (TextView) convertView
						.findViewById(android.R.id.text1);
				talkTitle.setText(talk.getTalkTitle());
				TextView talkDetails = (TextView) convertView
						.findViewById(android.R.id.text2);
				double startTime = talk.getTalkStartTime();
				double duration = talk.getTalkDurationInMins();
				talkDetails.setText("Start Time: " + startTime + "hrs"
						+ ", Duration: " + duration + "mins");
				return convertView;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				Session session = track.getSessions().get(groupPosition);
				// there will be no talks in breaks
				if (session.isConferenceBreak()) {
					return 0;
				}
				return track.getSessions().get(groupPosition).getTalks().size();
			}

			@Override
			public Object getGroup(int groupPosition) {
				return track.getSessions().get(groupPosition);
			}

			@Override
			public int getGroupCount() {
				return track.getSessions().size();
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = ((LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							.inflate(
									android.R.layout.simple_expandable_list_item_2,
									null);
					((TextView) convertView.findViewById(android.R.id.text1))
							.setTextColor(blackColor);
					((TextView) convertView.findViewById(android.R.id.text2))
							.setTextColor(blackColor);
				}
				Session session = (Session) getGroup(groupPosition);
				TextView sessionTitle = (TextView) convertView
						.findViewById(android.R.id.text1);

				if (session.isConferenceBreak()) {
					sessionTitle.setText("Break");
				} else {
					sessionTitle.setText("Session_" + groupPosition);
				}
				TextView sessionDetails = (TextView) convertView
						.findViewById(android.R.id.text2);

				double startTime = session.getSessionStartTime();
				double endTime = startTime
						+ (session.getSessionDurationInMins() / 60);
				sessionDetails.setText("Start Time: " + startTime + "hrs"
						+ ", End Time: " + endTime + "hrs");
				return convertView;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				// TODO Auto-generated method stub
				return false;
			}

		}
	}

	private class TrackAdapter extends FragmentStatePagerAdapter {

		public TrackAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return new TrackFragment(conference.getTracks().get(arg0), arg0);
		}

		@Override
		public int getCount() {
			return conference.getTracks().size();
		}

	}
}
