package com.internshala.flash.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internshala.flash.data.InternetItem
import com.internshala.flash.network.FlashApi
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashViewmodel : ViewModel() {
    private val _uiState = MutableStateFlow(FlashUiState())
    val uiState: StateFlow<FlashUiState> = _uiState.asStateFlow()

   private val _isVisible = MutableStateFlow(true)
    val isVisible = _isVisible

    var itemUiState : ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?> get() = _user

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: MutableStateFlow<String> get() = _phoneNumber

    private val _otp = MutableStateFlow("")
    val otp: MutableStateFlow<String> get() = _otp

    private val _cardItems = MutableStateFlow<List<InternetItem>>(emptyList())
    val cardItems: StateFlow<List<InternetItem>> get() = _cardItems.asStateFlow()

    private val _verificationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String> get() = _verificationId

    private val _ticks = MutableStateFlow(60L)
    val ticks: MutableStateFlow<Long> get() = _ticks

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> get() = _loading

    private val _logoutChecked = MutableStateFlow(false)
    val logoutChecked: MutableStateFlow<Boolean> get() = _logoutChecked


    private lateinit var timerjob: Job

    private val database = Firebase.database
    private val myRef = database.getReference("users/${auth.currentUser?.uid}")


    private lateinit var internetJob : Job
    private var screenJob : Job

    sealed interface ItemUiState {
        data class Success(val items: List<InternetItem>): ItemUiState
        data object Loading: ItemUiState
        data object Error : ItemUiState
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setOtp(otp: String) {
        _otp.value = otp
    }

    fun setVerificationId(verificationId: String) {
        _verificationId.value = verificationId
    }

    fun setUser(user: FirebaseUser) {
        _user.value = user
    }

    fun cleardata() {
        _phoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""
        _user.value = null
        resettimer()
    }

    fun runTimer() {
        timerjob = viewModelScope.launch {
            _ticks.value = 60L
            while (_ticks.value > 0) {
                delay(1000)
                _ticks.value -= 1
            }
        }
    }

    fun resettimer() {
        try {
            timerjob.cancel()
        } catch (exception: Exception) {
            _ticks.value = 60L
        }
    }

    fun setloading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun setLogOutStatus(
        logoutStatus: Boolean
    ) {
        _logoutChecked.value = logoutStatus
    }


    fun addToCart(item: InternetItem) {
        _cardItems.value += item
    }

    fun addToDatabase( item: InternetItem) {
        myRef.push().setValue(item)
    }

    private fun fillCartItems() {
        // Read from the database
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _cardItems.value = emptyList()
                for (childSnapshot in dataSnapshot.children) {
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let {
                        val newItem = it
                        addToCart(newItem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }



    fun removeFromCart(olditem: InternetItem) {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    var itemRemoved = false
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let {
                        if (olditem.itemName == it.itemName && olditem.itemPrice == it.itemPrice) {
                            childSnapshot.ref.removeValue()
                            itemRemoved = true
                        }
                    }
                    if (itemRemoved) break
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun updateClickText(updatedText: String) {
        _uiState.update {
        it.copy(
         clickStatus = updatedText
)
        }
    }

    fun updateselectedCategory(updatedText: Int) {
        _uiState.update {
            it.copy(
            selectedCategory = updatedText
            )
        }
    }

    fun getFlashItems() {
        internetJob = viewModelScope.launch {
            try {
                val listResults = FlashApi.retrofitService.getItems()
                itemUiState = ItemUiState.Success(listResults)
            }
            catch (exception: Exception) {
                itemUiState = ItemUiState.Error
                toggleVisibility()
                screenJob.cancel()
            }

        }
    }

    private fun toggleVisibility() {

        _isVisible.value = false
        }

    init{
        screenJob = viewModelScope.launch {
            delay(3000)
            toggleVisibility()
        }
        getFlashItems()
        fillCartItems()
    }
}
