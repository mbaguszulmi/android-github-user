package com.mbaguszulmi.githubuser.view.preferences

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.receiver.AlarmReceiver

class MainPreference: PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMINDER: String

    private lateinit var reminderPreference: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)

        alarmReceiver = AlarmReceiver()

        initPreference()
        setValues()
    }

    private fun setValues() {
        val sh = preferenceManager.sharedPreferences
        reminderPreference.isChecked = sh.getBoolean(REMINDER, false)
    }

    private fun initPreference() {
        REMINDER = resources.getString(R.string.key_sp_alarm)

        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == REMINDER) {
            val isReminderActive = sharedPreferences.getBoolean(REMINDER, false)
            reminderPreference.isChecked = isReminderActive

            if (isReminderActive && context != null) {
                alarmReceiver.setRepeatingAlarm(requireContext(), getString(R.string.reminder_time_value))
            }
            else {
                alarmReceiver.cancelAlarm(requireContext())
            }
        }
    }
}