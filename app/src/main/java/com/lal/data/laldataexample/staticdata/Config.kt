package staticdata

// Class contains static configuration keys
class Config{
    companion object {
        const val MOCK_API_URL: String = "https://mock-api-mobile.dev.lalamove.com/deliveries"
        const val OFFSET: String = "?offset="
        const val LIMIT: String = "&limit="
        const val MOCK_RETRIEVING_API: String = "mockRetrievingAPI"
        const val OFFSET_STRING: String = "offset"
        const val LIMIT_STRING: String = "limit"
        const val MOCK_REPO_FRAGMENT = "mockRepoFragment"
        const val MAP_FRAGMENT = "mapFragment"
        const val STATUS_ERROR = "statusError"
        const val MESSAGE_ERROR = "messageError"
        const val SHARED_PREFRENCE_NO_DATA_KEY_STRING = "0"
        const val SHARED_PREFRENCE_NO_DATA_KEY_INT = 0

    }
}