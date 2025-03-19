package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.res.stringResource
import com.example.FlyHigh.R

@Composable
fun PrivacyPolicyScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.privacy_policy_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(id = R.string.last_updated) + "\n\n" +
                    stringResource(id = R.string.information_collected_title) + "\n" +
                    stringResource(id = R.string.information_collected_content) + "\n" +
                    stringResource(id = R.string.usage_data_title) + "\n" +
                    stringResource(id = R.string.usage_data_content) + "\n\n" +
                    stringResource(id = R.string.usage_purpose_title) + "\n" +
                    stringResource(id = R.string.usage_purpose_content) + "\n\n" +
                    stringResource(id = R.string.sharing_info_title) + "\n" +
                    stringResource(id = R.string.sharing_info_content) + "\n\n" +
                    stringResource(id = R.string.security_title) + "\n" +
                    stringResource(id = R.string.security_content) + "\n\n" +
                    stringResource(id = R.string.rights_title) + "\n" +
                    stringResource(id = R.string.rights_content) + "\n\n" +
                    stringResource(id = R.string.cookies_title) + "\n" +
                    stringResource(id = R.string.cookies_content) + "\n\n" +
                    stringResource(id = R.string.third_party_links_title) + "\n" +
                    stringResource(id = R.string.third_party_links_content) + "\n\n" +
                    stringResource(id = R.string.privacy_policy_changes_title) + "\n" +
                    stringResource(id = R.string.privacy_policy_changes_content) + "\n\n" +
                    stringResource(id = R.string.contact_title) + "\n" +
                    stringResource(id = R.string.contact_content),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Volver a la pantalla anterior
        ElevatedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = stringResource(id = R.string.back_button), color = Color.White)
        }
    }
}
