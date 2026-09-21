package com.ethanjohnson.flipside.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.ui.theme.DustyRed
import flipside.shared.generated.resources.Res
import flipside.shared.generated.resources.add
import flipside.shared.generated.resources.collection
import flipside.shared.generated.resources.discover
import flipside.shared.generated.resources.home
import flipside.shared.generated.resources.wishlist
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private data class FlipSideNavigationItem(
    val destination: FlipSideDestination,
    val label: String,
    val icon: DrawableResource
)

private val navigationItems = listOf(
    FlipSideNavigationItem(
        destination = FlipSideDestination.HOME,
        label = "Home",
        icon = Res.drawable.home
    ),
    FlipSideNavigationItem(
        destination = FlipSideDestination.COLLECTION,
        label = "Collection",
        icon = Res.drawable.collection
    ),
    FlipSideNavigationItem(
        destination = FlipSideDestination.ADD,
        label = "Add",
        icon = Res.drawable.add
    ),
    FlipSideNavigationItem(
        destination = FlipSideDestination.WISHLIST,
        label = "Wishlist",
        icon = Res.drawable.wishlist
    ),
    FlipSideNavigationItem(
        destination = FlipSideDestination.DISCOVER,
        label = "Discover",
        icon = Res.drawable.discover
    )
)

@Composable
fun FlipSideNavBar(
    currentDestination: FlipSideDestination,
    onDestinationSelected: (FlipSideDestination) -> Unit
) {
    NavigationBar {
        navigationItems.forEach { item ->
            NavigationBarItem(
                selected =
                    currentDestination == item.destination,
                onClick = {
                    onDestinationSelected(
                        item.destination
                    )
                },
                icon = {
                    if (
                        item.destination ==
                        FlipSideDestination.ADD
                    ) {
                        AddNavigationIcon(
                            icon = item.icon,
                            contentDescription =
                                item.label
                        )
                    } else {
                        Icon(
                            painter = painterResource(
                                item.icon
                            ),
                            contentDescription =
                                item.label
                        )
                    }
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}

@Composable
fun FlipSideNavRail(
    currentDestination: FlipSideDestination,
    onDestinationSelected: (FlipSideDestination) -> Unit
) {
    NavigationRail(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight()
    ) {
        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {
            navigationItems.forEach { item ->
                NavigationRailItem(
                    selected =
                        currentDestination ==
                                item.destination,
                    onClick = {
                        onDestinationSelected(
                            item.destination
                        )
                    },
                    icon = {
                        if (
                            item.destination ==
                            FlipSideDestination.ADD
                        ) {
                            AddNavigationIcon(
                                icon = item.icon,
                                contentDescription =
                                    item.label
                            )
                        } else {
                            Icon(
                                painter =
                                    painterResource(
                                        item.icon
                                    ),
                                contentDescription =
                                    item.label
                            )
                        }
                    },
                    label = {
                        Text(item.label)
                    }
                )
            }
        }
    }
}

@Composable
private fun AddNavigationIcon(
    icon: DrawableResource,
    contentDescription: String
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        color = DustyRed,
        contentColor =
            MaterialTheme.colorScheme.onPrimary
    ) {
        Box(
            contentAlignment =
                Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription =
                    contentDescription,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}