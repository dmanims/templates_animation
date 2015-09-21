package com.example.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.R;
import com.example.utility.Preferences;


public class ExampleFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		addPreferencesFromResource(R.xml.prefs);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// register listener
		PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

		// bind data
		bindData();
	}


	@Override
	public void onDestroy()
	{
		super.onDestroy();

		// unregister listener
		PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		bindData();
	}


	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
	{
		super.onPreferenceTreeClick(preferenceScreen, preference);

		// if the user has clicked on a preference screen, setup the action bar
		if(preference instanceof PreferenceScreen)
		{
			setupActionBar((PreferenceScreen) preference);
		}

		return false;
	}


	private void setupActionBar(PreferenceScreen preferenceScreen)
	{
		final Dialog dialog = preferenceScreen.getDialog();
		Toolbar toolbar;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			LinearLayout root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent();
			toolbar = (Toolbar) LayoutInflater.from(getActivity()).inflate(R.layout.toolbar, root, false);
			root.addView(toolbar, 0);
		}
		else
		{
			ViewGroup root = (ViewGroup) dialog.findViewById(android.R.id.content);
			ListView content = (ListView) root.getChildAt(0);
			root.removeAllViews();
			toolbar = (Toolbar) LayoutInflater.from(getActivity()).inflate(R.layout.toolbar, root, false);

			int height;
			TypedValue typedValue = new TypedValue();
			if(getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true))
			{
				height = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
			}
			else
			{
				height = toolbar.getHeight();
			}

			content.setPadding(0, height, 0, 0);
			root.addView(content);
			root.addView(toolbar);
		}

		TypedArray typedArray = getActivity().getTheme().obtainStyledAttributes(R.style.Theme_Example_Light, new int[]{R.attr.homeAsUpIndicator});
		int attributeResourceId = typedArray.getResourceId(0, 0);
		Drawable drawable = ContextCompat.getDrawable(getActivity(), attributeResourceId);

		toolbar.setTitle(preferenceScreen.getTitle());
		toolbar.setNavigationIcon(drawable);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
	}


	private void bindData()
	{
		// references
		PreferenceScreen rootPreferenceScreen = getPreferenceScreen();
		EditTextPreference displayNameEditTextPreference = (EditTextPreference) findPreference(getString(R.string.prefs_key_display_name));

		// preferences
		Preferences preferences = new Preferences(getActivity());

		// summary
		displayNameEditTextPreference.setSummary(preferences.getDisplayName());
	}
}
