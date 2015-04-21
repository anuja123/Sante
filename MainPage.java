package com.example.sante;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class MainPage extends ActionBarActivity implements
		android.support.v7.app.ActionBar.TabListener {
	// Within which the entire activity is enclosed
	private ViewPager viewPager;
	// private TabsPagerAdapter mAdapter;
	DrawerLayout mDrawerLayout;
	private android.support.v4.app.Fragment mFragment;
	// ListView represents Navigation Drawer
	ListView mDrawerList;
	List<Fragment> fragList = new ArrayList<Fragment>();
	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the
	// action bar
	ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	String mTitle = "";
	// android.app.ActionBar actionBar = getActionBar();
	// Title navigation Spinner data
	

	// Navigation adapter
	private TitleNavigationAdapter adapter;
	private TabsPagerAdapter mAdapter;
	// Refresh menu item
	private MenuItem refreshMenuItem;

	static public class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		public TabListener(Activity activity, String tag, Class<T> pClass) {
			mActivity = activity;
			mTag = tag;
			mClass = pClass;
		}

		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, attach it in order to show it
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is about to be
				// attached.
				ft.detach(mFragment);
			}
		}

		@Override
		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// Do nothing.
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		final ActionBar actionBar = getSupportActionBar();
		
		if (viewPager != null) {
			Log.e("Error", "ViewPager is null");
		}
		

		if (mAdapter != null) {
			Log.e("Error", "mAdapter is null");
		}
		
		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab = actionBar
				.newTab()
				.setText("Bars & Pubs")
				.setTabListener(
						new TabListener<PlaceholderFragment>(this,
								"bars & pubs", PlaceholderFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText("Night Clubs")
				.setTabListener(
						new TabListener<PlaceholderFragment>(this,
								"night clubs", PlaceholderFragment.class));
		actionBar.addTab(tab);

		mTitle = (String) getTitle();

		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		/*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

*/		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open, R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {

				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

		};

		getSupportActionBar().setHomeAsUpIndicator(
				getResources().getDrawable(R.drawable.ic_drawer));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Spinner title navigation data

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Creating an ArrayAdapter to add items to the listview mDrawerList
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.drawer_list_item, getResources()
						.getStringArray(R.array.Side_Menu));

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);

		// Enabling Home button
		// getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Getting an array of rivers
				String[] rivers = getResources().getStringArray(
						R.array.Side_Menu);

				// Currently selected river
				mTitle = rivers[position];

				// Creating a fragment object
				MenuFragment mFragment = new MenuFragment();

				// Creating a Bundle object
				Bundle data = new Bundle();

				// Setting the index of the currently selected item of
				// mDrawerList
				data.putInt("position", position);

				// Setting the position to the fragment
				mFragment.setArguments(data);

				// Getting reference to the FragmentManager
				android.app.FragmentManager fragmentManager = getFragmentManager();

				// Creating a fragment transaction
				android.app.FragmentTransaction ft = fragmentManager
						.beginTransaction();

				// Adding a fragment to the fragment transaction
				ft.replace(R.id.content_frame, mFragment);

				// Committing the transaction
				ft.commit();

				// Closing the drawer
				mDrawerLayout.closeDrawer(mDrawerList);

			}
		});
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment, container,
					false);
			TextView outputTextView = (TextView) rootView
					.findViewById(R.id.msg);
			outputTextView.setText("");
			outputTextView.setText("Hello " + getTag());
			return rootView;
		}
	}

	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/** Handling the touch event of app icon */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

		// MenuItem searchItem = menu.findItem(R.id.action_search);
		// SearchView searchView = (SearchView)
		// MenuItemCompat.getActionView(searchItem);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		// searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getCallingActivity()));
		// searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(new ComponentName(this,
						SearchResultsActivity.class)));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

}
