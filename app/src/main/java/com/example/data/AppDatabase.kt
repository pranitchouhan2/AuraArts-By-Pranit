package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Artwork::class,
        CommissionInquiry::class,
        CartItem::class,
        AppNotification::class,
        ArtReel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aura_arts_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.appDao())
                }
            }
        }

        private suspend fun populateDatabase(dao: AppDao) {
            // Prepopulate Artworks
            val initialArtworks = listOf(
                Artwork(
                    id = "shiva_1",
                    title = "Adiyogi Cosmic Flow",
                    story = "A vibrant representation of Adiyogi Shiva in a deep meditative state, draped in cosmic celestial purple of the universe.",
                    category = "Shiva Art",
                    imageUrl = "shiva_cosmic_flow",
                    materials = "Acrylic on Canvas, Gesso, Varnish",
                    creationTime = "18 Hours",
                    price = 450.0,
                    likesCount = 280,
                    isLiked = false,
                    isSaved = false
                ),
                Artwork(
                    id = "shiva_2",
                    title = "The Tandava Energy",
                    story = "Depicting the divine dance of creation, preservation, and dissolution. Vibrant neon violet with soft cyan details.",
                    category = "Shiva Art",
                    imageUrl = "shiva_tandava",
                    materials = "Graphite, Ink, Oil pastel elements",
                    creationTime = "24 Hours",
                    price = 620.0,
                    likesCount = 390,
                    isLiked = false,
                    isSaved = false
                ),
                Artwork(
                    id = "mandala_1",
                    title = "Aura Meditation Mandala",
                    story = "Symmetrical radial energy grid constructed meticulously using fine liner purple & pastel pink watercolor washing backgrounds.",
                    category = "Mandala Art",
                    imageUrl = "mandala_aura",
                    materials = "Special archival ink, heavy paper",
                    creationTime = "15 Hours",
                    price = 280.0,
                    likesCount = 145,
                    isLiked = false,
                    isSaved = false
                ),
                Artwork(
                    id = "mandala_2",
                    title = "Sahasrara Lotus Spiral",
                    story = "A thousand-petaled infinite circle mandala depicting the crown chakra. Created for spiritual healing and aesthetic calmness.",
                    category = "Mandala Art",
                    imageUrl = "mandala_sahasrara",
                    materials = "Fineliners, Acrylic fluid drops",
                    creationTime = "32 Hours",
                    price = 750.0,
                    likesCount = 512,
                    isLiked = false,
                    isSaved = false
                ),
                Artwork(
                    id = "portrait_1",
                    title = "Ethereal Musings Portrait",
                    story = "A semi-realistic soft graphite sketch with pastel pink aura highlights representing human thought process in beautiful clarity.",
                    category = "Portrait Sketches",
                    imageUrl = "portrait_musings",
                    materials = "Graphite (2B-8B), Charcoal, Soft dry pastels",
                    creationTime = "9 Hours",
                    price = 190.0,
                    likesCount = 98,
                    isLiked = false,
                    isSaved = false
                ),
                Artwork(
                    id = "portrait_2",
                    title = "Inner Grace Portrait",
                    story = "Bespoke custom order sketch showing high-fidelity micro textures on high-quality cartridge sheets.",
                    category = "Portrait Sketches",
                    imageUrl = "portrait_inner_grace",
                    materials = "Cartridge heavy paper, carbon pencils",
                    creationTime = "12 Hours",
                    price = 240.0,
                    likesCount = 112,
                    isLiked = false,
                    isSaved = false
                ),
                Artwork(
                    id = "craft_1",
                    title = "Zen Lavender Clay Canvas",
                    story = "A physical combined sculpture craft on standard stretched frame. Lavender dried elements structured elegantly.",
                    category = "Handmade Crafts",
                    imageUrl = "craft_zen_lavender",
                    materials = "Resin elements, Clay shapes, Canvas border",
                    creationTime = "14 Hours",
                    price = 320.0,
                    likesCount = 167,
                    isLiked = false,
                    isSaved = false
                ),
                Artwork(
                    id = "decor_1",
                    title = "Signature Wall Panel",
                    story = "Signature wall panel combining watercolor fluid splashes, multi-layered mandala overlays, and beautiful spiritual quotes by Pranit.",
                    category = "Wall Decor",
                    imageUrl = "decor_signature",
                    materials = "Wooden structural board, fluid acrylics",
                    creationTime = "40 Hours",
                    price = 1200.0,
                    likesCount = 890,
                    isLiked = false,
                    isSaved = false
                )
            )
            dao.insertArtworks(initialArtworks)

            // Prepopulate Reels
            val initialReels = listOf(
                ArtReel(
                    id = "reel_1",
                    title = "Painting Adiyogi Shiva",
                    description = "Watch the watercolor process flow starting from loose pencil guidelines to final layered detailed ink shading. Acrylic on Canvas by Pranit Chouhan.",
                    thumbnailColor = "purple"
                ),
                ArtReel(
                    id = "reel_2",
                    title = "Symmetry Harmony Mandala",
                    description = "Timelapse of drawing a microscopic segments. Mesmerizing radial lines and spiritual geometry coming together in real-time.",
                    thumbnailColor = "blue"
                ),
                ArtReel(
                    id = "reel_3",
                    title = "Bespoke Clay & Canvas Crafting",
                    description = "Blending heavy clay textures into standard paintings. Textured layered brushstrokes detail on Aura Arts background canvas.",
                    thumbnailColor = "pink"
                )
            )
            dao.insertReels(initialReels)

            // Prepopulate Notifications
            val initialNotifications = listOf(
                AppNotification(
                    title = "Welcome, Art Lover!",
                    text = "Unlock modern handmade spiritual canvas arts. Thank you for installing the official Aura Arts application. Direct contact is live via Chat!",
                    iconType = "message"
                ),
                AppNotification(
                    title = "New Art Series Drops!",
                    text = "Aura Meditation Mandala is trending now! Catch the mesmerizing creation reel in our Feed.",
                    iconType = "trending"
                ),
                AppNotification(
                    title = "Shiva Art Offer",
                    text = "Special 10% launch discount on all handmade Shiva canvases. Use order inquiry buttons to custom-book directly.",
                    iconType = "offer"
                )
            )
            for (notif in initialNotifications) {
                dao.insertNotification(notif)
            }
        }
    }
}
