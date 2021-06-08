# CreDust
Proof of concept built as Capstone Project for the Bangkit Program. CreDust is an application that enables users to scan trash, get information about the material of the trash, 
and get DIY project recommendations. We decided that the application will have 5 functions:
1.  first there’s the feed that enables user to see all of other people’s creations,
1.  then there’s the explore page where users can search for DIY projects and favorite it,
1.  then there’s the scanning feature where users can scan trash and then get DIY project recommendations,
1.  rewards, where users can see their points (recycling points) and redeem it into voucher,
1.  then profile, where there’s profile management features.

We decided those features based on the flow diagram that we have made: 
![Flow Diagram](https://drive.google.com/uc?export=view&id=1baIKoybOlDX8i07FhIbyN1TPN1fJZWRW)

Here's how every little bits and pieces work:
1.  For this proof of concept, we only used Room local database because of the time constraint, the local database stores the information of the available DIY project instructions.
    Here's the table of the database: 
    * id (integer)
    * title (string)
    * image (string, uri)
    * plastic (boolean flag to decide whether the DIY project uses plastic)
    * glass (boolean flag to decide whether the DIY project uses glass)
    * metal (boolean flag to decide whether the DIY project uses metal)
    * points (integer, the amount of points that will be rewarded if users do the DIY project)
    * materials (list of strings containing materials for the DIY project, stored as JSON string)
    * instructions (list of strings containing instructions to do the DIY project, stored as JSON string)
    * favorite (boolean flag to determine whether item is bookmarked or not)

    In order to be able to store list of strings, we created a type converter that will convert list of strings to JSON string and vice versa.
    On the creation of the database, we pre-populated the database with local data in the form of .JSON, that you can see in the assets folder. We pre-populated the
    database by adding a callback in ProjectRoomDatabase.kt on the getDatabase() method. In the future, we should be able to implement an online database such as firebase, etc.
1.  Home Fragment, we only made it as a mockup because of the time constraint. In the future, we should be able to implement this feature. Users will be able to share their creations
    hopefully this feature will be able to inspire other people to do recycling project.
1.  Explore Fragment, is where users will be able to look up DIY projects, we haven't implemented the searchview but the recycler view is already working. We also use MVVM architecture here,
    the data will be carried inside the view model while the ui is in the Explore Fragment itself. We implemented bookmarks by using favorite flag in the view model.
1.  Camera Fragment, is where users will be able to press a button to scan trash. We just create an camera intent and then we have to define the file name and where we will store the image picture.
    We launch the activity by using startActivityForResult()
1.  Camera Results Fragment, how can we send an image from our phone to the API? It is simple, because the api only accepts jpeg, jpg, or png format, 
    we have to convert the image taken inside our app into that format, we can do that by converting the image data to byte stream, then we can compress the image into JPEG. 
    We use quality 25%, a quarter of the uncompressed quality, so that it’s faster to upload. The API also categorizes the results based on the file name, so we have to create the file name dynamically, 
    we don’t want an image file that is uploaded to have the same name because it will just replace the previous scan results, we don’t want that to happen. Then we use okhttpclient to to create a post request to the api endpoint,
    with the image being attached on the request body. In the camera results view model, we can access the repository to get recommended projects. We used the output from the API as an input for the getRecommendedProjects() method.
1.  We used a raw query to get the desired DIY projects. The implementation is inside the repository, on the getRecommendedProjects(), we use nested condition to achieve the desired query. The output will then be displayed on the recycler view in
    the Camera Results Fragment.
1.  Rewards fragment is only a mockup because of the time constraints and the complexity of the business process.
1.  Main Activity is the host activity, it dictates what fragment are currently active. We use bottom navbar from material components.

How to use:
1.  Run application.
1.  Press the camera icon on the bottom navigation bar.
1.  Scan trashes around you.
1.  Get DIY project recommendations.

Things to do improve in the future:
1.  Implement feeds.
1.  Implement combination of online-offline database.
1.  Implement user management system.
1.  Implement rewards.
1.  Beautify the UI with animations.
