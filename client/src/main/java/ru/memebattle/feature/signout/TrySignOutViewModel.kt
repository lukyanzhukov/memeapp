package ru.memebattle.feature.signout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import client.common.presentation.PlatformSingleLiveEvent

class TrySignOutViewModel : ViewModel() {

	private val _signOutEvent = PlatformSingleLiveEvent<Unit>()
	val signOutEvent: LiveData<Unit>
		get() = _signOutEvent

	fun onSignOut() {
		_signOutEvent.call()
	}
}