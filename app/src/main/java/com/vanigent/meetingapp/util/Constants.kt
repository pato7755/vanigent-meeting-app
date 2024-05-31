package com.vanigent.meetingapp.util

object Constants {

    const val SEVENTY_PERCENT = 0.7f
    const val THIRTY_PERCENT = 0.3f
    const val FIVE_HUNDRED = 500
    const val MAXIMUM_ALLOWED_COST_PER_MEAL = 182.00
    const val LOGIN_URL = "https://prod-77.westus.logic.azure.com:443/workflows/ec0fbdf53a674766b09769eb01c700af/" +
            "triggers/manual/paths/invoke?" +
            "api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&" +
            "sv=1.0&sig=YSS20JuZBCIkS2GzTJGH7WPd_uVVPwFuzkcA9sH3JxM"
    const val UPLOAD_PDF_URL = "https://prod-22.westus.logic.azure.com:443/workflows/f554dc423df44558b2fb71d0aa7e48c8/" +
            "triggers/manual/paths/invoke?" +
            "api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&" +
            "sv=1.0&sig=qSD-j5Zy2puxvNT_6KFttUG8T9TGCbnG4D3HzAJOu6U"
}
