package com.example.luxorappandroid.ui.welcome

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.example.luxorappandroid.R
import androidx.compose.ui.res.stringResource

data class WelcomePageData(
    @DrawableRes val image: Int,
    val title: String,
    val subtitle: String,
    val description: String
)

object WelcomePages {
    @Composable
    fun getPages(): List<WelcomePageData> {
        return listOf(
            WelcomePageData(
                image = R.drawable.temp1,
                title = stringResource(R.string.title1),
                subtitle = stringResource(R.string.subTitle1),
                description = stringResource(R.string.description1)
            ),
            WelcomePageData(
                image = R.drawable.temp2,
                title = stringResource(R.string.title2),
                subtitle = stringResource(R.string.subTitle2),
                description = stringResource(R.string.description2)
            ),
            WelcomePageData(
                image = R.drawable.temp3,
                title = stringResource(R.string.title3),
                subtitle = stringResource(R.string.subTitle3),
                description = stringResource(R.string.description3)
            ),
            WelcomePageData(
                image = R.drawable.temp4,
                title = stringResource(R.string.title4),
                subtitle = stringResource(R.string.subTitle4),
                description = stringResource(R.string/)
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