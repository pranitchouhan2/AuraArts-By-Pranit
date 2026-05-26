package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AppRepository(database.appDao())

    // UI States
    val artworks: StateFlow<List<Artwork>> = repository.artworks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inquiries: StateFlow<List<CommissionInquiry>> = repository.inquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<AppNotification>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reels: StateFlow<List<ArtReel>> = repository.reels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and Filters
    private val _gallerySearchQuery = MutableStateFlow("")
    val gallerySearchQuery = _gallerySearchQuery.asStateFlow()

    private val _gallerySelectedCategory = MutableStateFlow("All")
    val gallerySelectedCategory = _gallerySelectedCategory.asStateFlow()

    // Filtered Artworks for Pinterest-Style Gallery
    val filteredArtworks: StateFlow<List<Artwork>> = combine(
        artworks, _gallerySearchQuery, _gallerySelectedCategory
    ) { artList, query, category ->
        artList.filter { art ->
            (category == "All" || art.category.equals(category, ignoreCase = true)) &&
            (query.isEmpty() || art.title.contains(query, ignoreCase = true) || art.story.contains(query, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // App Preferences (Dark Mode & Creative Accent)
    private val _isDarkMode = MutableStateFlow<Boolean?>(null) // null means auto
    val isDarkMode = _isDarkMode.asStateFlow()

    private val _customThemeAccent = MutableStateFlow("Aura Classic") // Classic, Lavender, Soft Blue, Pastel Pink
    val customThemeAccent = _customThemeAccent.asStateFlow()

    // Notification Settings
    private val _pushNotificationsEnabled = MutableStateFlow(true)
    val pushNotificationsEnabled = _pushNotificationsEnabled.asStateFlow()

    private val _orderUpdatesEnabled = MutableStateFlow(true)
    val orderUpdatesEnabled = _orderUpdatesEnabled.asStateFlow()

    fun updateGallerySearch(query: String) {
        _gallerySearchQuery.value = query
    }

    fun updateGalleryCategory(category: String) {
        _gallerySelectedCategory.value = category
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun setThemeAccent(accent: String) {
        _customThemeAccent.value = accent
    }

    fun togglePushNotifications(enabled: Boolean) {
        _pushNotificationsEnabled.value = enabled
    }

    fun toggleOrderUpdates(enabled: Boolean) {
        _orderUpdatesEnabled.value = enabled
    }

    // Cart Management
    fun addToCart(artwork: Artwork) {
        viewModelScope.launch {
            val existing = cartItems.value.find { it.id == artwork.id }
            if (existing != null) {
                repository.updateCartItemQuantity(artwork.id, existing.quantity + 1)
            } else {
                repository.insertCartItem(
                    CartItem(
                        id = artwork.id,
                        title = artwork.title,
                        price = artwork.price,
                        imageUrl = artwork.imageUrl,
                        quantity = 1
                    )
                )
            }
            // Trigger local notification
            repository.insertNotification(
                AppNotification(
                    title = "Added to Cart!",
                    text = "\"${artwork.title}\" has been added to your shopping bag.",
                    iconType = "order"
                )
            )
        }
    }

    fun increaseQuantity(itemId: String) {
        viewModelScope.launch {
            cartItems.value.find { it.id == itemId }?.let {
                repository.updateCartItemQuantity(itemId, it.quantity + 1)
            }
        }
    }

    fun decreaseQuantity(itemId: String) {
        viewModelScope.launch {
            cartItems.value.find { it.id == itemId }?.let {
                if (it.quantity > 1) {
                    repository.updateCartItemQuantity(itemId, it.quantity - 1)
                } else {
                    repository.deleteCartItem(itemId)
                }
            }
        }
    }

    fun removeFromCart(itemId: String) {
        viewModelScope.launch {
            repository.deleteCartItem(itemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Interactivity
    fun toggleLikeArtwork(artworkId: String, currentVal: Boolean) {
        viewModelScope.launch {
            repository.toggleLikeArtwork(artworkId, !currentVal)
            // Trigger local notification on liked
            if (!currentVal) {
                val art = artworks.value.find { it.id == artworkId }
                repository.insertNotification(
                    AppNotification(
                        title = "New Like Recorded",
                        text = "You liked \"${art?.title ?: "an artwork"}\". Thank you for supporting Pranit's creations!",
                        iconType = "like"
                    )
                )
            }
        }
    }

    fun toggleSaveArtwork(artworkId: String, currentVal: Boolean) {
        viewModelScope.launch {
            repository.toggleSaveArtwork(artworkId, !currentVal)
        }
    }

    fun toggleLikeReel(reelId: String, currentVal: Boolean) {
        viewModelScope.launch {
            repository.toggleLikeReel(reelId, !currentVal)
        }
    }

    // Submit Custom Commission Inquiry
    fun submitCommissionInquiry(desc: String, style: String, size: String, budget: String, name: String) {
        viewModelScope.launch {
            val inquiry = CommissionInquiry(
                description = desc,
                artStyle = style,
                size = size,
                budget = budget,
                contactName = name
            )
            repository.insertInquiry(inquiry)

            // Trigger notification
            repository.insertNotification(
                AppNotification(
                    title = "Inquiry Saved Offline",
                    text = "Your request for \"$style ($size)\" has been stored securely. WhatsApp setup is now active.",
                    iconType = "order"
                )
            )
        }
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }
}
