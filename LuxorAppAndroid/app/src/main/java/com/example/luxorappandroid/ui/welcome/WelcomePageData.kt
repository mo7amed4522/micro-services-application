package com.example.luxorappandroid.ui.welcome

import androidx.annotation.DrawableRes
import com.example.luxorappandroid.R

data class WelcomePageData(
    @DrawableRes val image: Int,
    val title: String,
    val subtitle: String,
    val description: String
)

// Welcome pages data matching iOS app
object WelcomePages {
    fun getPages(): List<WelcomePageData> {
        return listOf(
            WelcomePageData(
                image = R.drawable.temp1,
                title = "Discover Ancient Wonders",
                subtitle = "Journey Through Time",
                description = "Explore the magnificent temples, tombs, and monuments of ancient Luxor with our comprehensive guide."
            ),
            WelcomePageData(
                image = R.drawable.temp2,
                title = "Expert Local Guides",
                subtitle = "Authentic Experiences",
                description = "Connect with knowledgeable local guides who bring the rich history of Luxor to life with captivating stories."
            ),
            WelcomePageData(
                image = R.drawable.temp3,
                title = "Hidden Treasures",
                subtitle = "Off the Beaten Path",
                description = "Discover secret locations and lesser-known archaeological sites that most tourists never get to see."
            ),
            WelcomePageData(
                image = R.drawable.temp4,
                title = "Cultural Immersion",
                subtitle = "Live Like a Local",
                description = "Experience authentic Egyptian culture through traditional meals, local markets, and cultural activities."
            ),
            WelcomePageData(
                image = R.drawable.temp5,
                title = "Personalized Tours",
                subtitle = "Your Perfect Journey",
                description = "Customize your adventure with flexible itineraries designed to match your interests and schedule."
            ),
            WelcomePageData(
                image = R.drawable.temp6,
                title = "Start Your Adventure",
                subtitle = "Welcome to Luxor",
                description = "Begin your unforgettable journey through one of the world's greatest open-air museums."
            )
        )
    }
}