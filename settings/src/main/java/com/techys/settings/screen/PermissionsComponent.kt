package com.techys.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.settings.R
import com.techys.settings.theme.AppTheme
import com.techys.settings.theme.Dimen

@Composable
fun PermissionsComponent(modifier: Modifier = Modifier) {
    Card (modifier = modifier,
        shape = MaterialTheme.shapes.large) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(Dimen.medium)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.small)
                    .wrapContentHeight()) {
                Text(text = stringResource(R.string.permission_notification))
                Spacer(modifier = Modifier.weight(1f))
                //compose version of fill parent not max size but what other component take and this would fill
                VerticalDivider(modifier = Modifier.height(48.dp).padding(vertical = Dimen.medium))
                Spacer(modifier = Modifier.width(Dimen.medium))
                Switch(checked = true,
                    onCheckedChange = {}
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.small)) {
                Text(text = stringResource(R.string.permission_battery))
                Spacer(modifier = Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.height(48.dp).padding(vertical = Dimen.medium))
                Spacer(modifier = Modifier.width(Dimen.medium))
                Switch(checked = true,
                    onCheckedChange = {}
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.small)) {
                Text(text = stringResource(R.string.permission_system_overlay))
                Spacer(modifier = Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.height(48.dp).padding(vertical = Dimen.medium))
                Spacer(modifier = Modifier.width(Dimen.medium))
                Switch(checked = true,
                    onCheckedChange = {}
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        Surface {
            PermissionsComponent()
        }
    }
}