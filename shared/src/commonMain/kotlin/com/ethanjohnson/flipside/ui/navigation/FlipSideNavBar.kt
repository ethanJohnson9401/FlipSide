package com.ethanjohnson.flipside.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

import flipside.shared.generated.resources.Res
import flipside.shared.generated.resources.add
import flipside.shared.generated.resources.collection
import flipside.shared.generated.resources.discover
import flipside.shared.generated.resources.home
import flipside.shared.generated.resources.wishlist

private fun iconFor(
    destination: FlipSideDestination
): DrawableResource {
    return when (destination) {
        FlipSideDestination.HOME -> Res.drawable.home
        FlipSideDestination.COLLECTION -> Res.drawable.collection
        FlipSideDestination.ADD -> Res.drawable.add
        FlipSideDestination.WISHLIST -> Res.drawable.wishlist
        FlipSideDestination.DISCOVER -> Res.drawable.discover
    }
}

@Composable
fun FlipSideBottomBar(
    currentDestination: FlipSideDestination,
    onDestinationSelected: (FlipSideDestination) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        FlipSideDestination.entries.forEach { destination ->

            if (destination == FlipSideDestination.ADD) {
                NavigationBarItem(
                    selected = currentDestination == destination,
                    onClick = {
                        onDestinationSelected(destination)
                    },
                    icon = {
                        FloatingActionButton(
                            onClick = {
                                onDestinationSelected(destination)
                            },
                            modifier = Modifier.size(52.dp),
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.add),
                                contentDescription = "Add media"
                            )
                        }
                    },
                    label = {
                        Text("Add")
                    }
                )
            } else {
                NavigationBarItem(
                    selected = currentDestination == destination,
                    onClick = {
                        onDestinationSelected(destination)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(iconFor(destination)),
                            contentDescription = destination.label
                        )
                    },
                    label = {
                        Text(destination.label)
                    }
                )
            }
        }
    }
}

@Composable
fun FlipSideNavigationRail(
    currentDestination: FlipSideDestination,
    onDestinationSelected: (FlipSideDestination) -> Unit
) {
    NavigationRail(
        modifier = Modifier.width(88.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        FlipSideDestination.entries.forEach { destination ->

            if (destination == FlipSideDestination.ADD) {
                NavigationRailItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    selected = currentDestination == destination,
                    onClick = {
                        onDestinationSelected(destination)
                    },
                    icon = {
                        FloatingActionButton(
                            onClick = {
                                onDestinationSelected(destination)
                            },
                            modifier = Modifier.size(50.dp),
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.add),
                                contentDescription = "Add media"
                            )
                        }
                    },
                    label = {
                        Text("Add")
                    }
                )
            } else {
                NavigationRailItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    selected = currentDestination == destination,
                    onClick = {
                        onDestinationSelected(destination)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(iconFor(destination)),
                            contentDescription = destination.label
                        )
                    },
                    label = {
                        Text(destination.label)
                    }
                )
            }
        }
    }
}