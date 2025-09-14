# Android-QT-Client
Client-mode Android App for the Quality Treshold clustering algorithm in java (University Project)

# 📱 App

The App is a simple interface with a dashboard on the bottom. Here's a quick preview:

<video controls src="media/qtapp.mp4" title="Preview App"></video>

The dashboard left button is the first section you need to interact with in order to use the app:
- Once you open the app this screen is going to pop up automatically, so simply connect to the Server previously turned on by inserting IP and Port and clicking the button. If the client doesn't connect within the first 3 seconds it's likely that the server can't be reached because of a network issue or simply because the IP/Port are wrong, so just try again with different options.
- After the connection a message and a Toast pop-up will be shown underneath. Now you can use the other 2 buttons on the dashboard. If you try to access them before connecting to the server, the buttons will simply load white pages and a warning will pop up.
- The Database section can be accessed by clicking the middle button on the dashboard, once opened this section will prompt you for a table name, which corresponds to a database table on the Mysql instance connected to the server. If the table is spelled incorrectly/doesn't exist/is unreachable, the app will show an error and a toast pop-up, else all the table informations will be shown below on a simple Vertical ScrollView bar.
- Below the table name you can insert the radius to calculate the clusters, in case of an error a toast pop-up and a short description will appear. After the clusters are calculated, they are displayed in the previous ScrollView bar as well as the centroids and average distance.
- On the bottom of the page you can choose to save the clusters in a file with a custom file name, if you don't pick a name but simply press the save button, the file name will be the default one.
- The file section can be accessed by pressing the right button on the dashboard, once opened this section will prompt you for the file name, once inserting the correct name and clicking the button the clusters' centroids will be shown in a Vertical ScrollView bar.


# 💻 Code & Algorithm
a
