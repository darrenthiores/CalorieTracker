package com.dev.tracker_presentation.tracker_overview.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.dev.core.R
import com.dev.core_ui.LocalSpacing
import com.dev.tracker_domain.model.TrackedFood
import com.dev.tracker_presentation.component.NutrientInfo

@Composable
fun TrackerFoodItem(
    modifier: Modifier = Modifier,
    trackedFood: TrackedFood,
    onDeleteClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .padding(spacing.spaceExtraSmall)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .background(MaterialTheme.colors.surface)
            .padding(end = spacing.spaceMedium)
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)),
            painter = rememberAsyncImagePainter(
                model = ImageRequest
                    .Builder(context)
                    .data(trackedFood.imageUrl)
                    .crossfade(true)
                    .error(R.drawable.ic_burger)
                    .fallback(R.drawable.ic_burger)
                    .build()
            ),
            contentDescription = trackedFood.name,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(spacing.spaceMedium))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                modifier = Modifier,
                text = trackedFood.name,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))

            Text(
                modifier = Modifier,
                text = stringResource(
                    id = R.string.nutrient_info,
                    trackedFood.amount,
                    trackedFood.calories
                ),
                style = MaterialTheme.typography.body2
            )
        }

        Spacer(modifier = Modifier.width(spacing.spaceMedium))

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        onDeleteClick()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.delete)
            )

            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                NutrientInfo(
                    amount = trackedFood.carbs,
                    unit = stringResource(id = R.string.grams),
                    name = stringResource(id = R.string.carbs),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.width(spacing.spaceSmall))

                NutrientInfo(
                    amount = trackedFood.protein,
                    unit = stringResource(id = R.string.grams),
                    name = stringResource(id = R.string.protein),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.width(spacing.spaceSmall))

                NutrientInfo(
                    amount = trackedFood.fat,
                    unit = stringResource(id = R.string.grams),
                    name = stringResource(id = R.string.fat),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )
            }
        }
    }
}