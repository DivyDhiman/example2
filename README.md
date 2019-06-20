Steps 
1: login or sign up too google developer console - https://accounts.google.com/ServiceLogin/signinchooser?service=cloudconsole&passive=1209600&osid=1&continue=https%3A%2F%2Fconsole.developers.google.com%2F%3Fref%3Dhttps%3A%2F%2Fwww.google.com%2F&followup=https%3A%2F%2Fconsole.developers.google.com%2F%3Fref%3Dhttps%3A%2F%2Fwww.google.com%2F&flowName=GlifWebSignIn&flowEntry=ServiceLogin
2: Create a project 
3: Go to credentials inside the project 
4: Select the credential tab and select android key
5: Create an Android key and put restriction for android if you want to run on other then put restriction according to your platform
6: After selection put your package name and sha1 key in fingerprints(if you project is signed then put signed sha1 key if it's in debug mode then put debug sha1 key) and then save it
7: Then select library tab 
8: Then enable google map API for Android from libary
9: Then you will get the key in credential tab so copy that key and use it manifest within the tag
   <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="paste your key" />
