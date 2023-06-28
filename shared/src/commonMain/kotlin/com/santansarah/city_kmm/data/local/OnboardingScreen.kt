package com.santansarah.city_kmm.data.local

/**
 * All of the data elements for an Onboarding screen.
 * This data comes from app resources.
 */
data class OnboardingScreen(
    val currentScreen: Int,
    val headingIcon: Int,
    val headingText: Int,
    val subHeadingText: Int,
    val cardHeading: Int,
    val cardSubHeadingOne: Int,
    val cardSubHeadingTwo: Int,
    val cardDetails: CardDetailsOption
)

/**
 * Card details can include an icon with text, or a row, for example
 * Population: 75000.
 */
sealed class CardDetailsOption {
    data class IconListItem(val options: List<Pair<Int, Int>>) : CardDetailsOption()
    data class RowItem(val options: List<Pair<Int, Int>>): CardDetailsOption()
}
