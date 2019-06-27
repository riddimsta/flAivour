# flAivour

 
Manual


Install:

1. Clone or download repository to Android Studio workspace folder
2. Open the project with android studio by using File → open
3. Navigate to the project folder and select fridge_merged
4. Change the SDK setting in local.properties to your SDK folder
5. Restart Android SDK and click file → Sync project with gradle files
6. Plug in a smartphone device via USB
7. Wait until the syncing process is done
8. Launch the application in android studio and choose your smartphone device


Testing parametes:
Working API Levels: 26, 24
API Levels with issues: 28 (on virtual device)

Known issues:
The application may not gather the recipe images in an virtual machine. 
It seems that Glide has an issue with virtual ADB‘s so we recommend to 
plug in a smartphone device to test the application. The Food2Fork API 
only provides 50 requests per day for free users. If all requests are 
used you will not get a result from Food2Fork.

Activities:
Main Activity: Start screen with logo und button to get to the next activity.
Scanning: Scanning/detecting and adding new ingredients. Button to go to the ingredient list.
Ingredient list: Toggle buttons to decide with which ingredients recipes should be found. 
If no recipes are found - redirecting to ‘ingredient list’.
Recipe overview: List, sorted by social rank, with recommended recipes. Recipes also include 
other ingredients which were not scanned due to the fact that the pre trained TensorFlow model 
is not very good in detecting food items.
Recipes details: List of all ingredients and directions on how to prepare them.

Code Snippets:
Tensorflow and custom camera kit – modified to fit our project
https://github.com/amitshekhariitbhu/Android-TensorFlow-Lite-Example
https://camerakit.io

Calling custom camera kit. If an image is taken, it gets scaled, converted to a bytebuffer 
and sorted by colour (edge detection). TensorFlow interprets the bytebuffer; goes through 
the labels.txt and calculates with what probabilities the objects in the taken image might 
have with the labels in the labels.txt. The detected object with the highest probability 
gets saved as a string to a (ingredient) list.
 
Services:
RecipeRequestService:
The Service provides methods to collect Data about Recipes by a JSON request.
CollectRecipes need a list of ingredients (Strings) and an eventHandler. The EventHandler 
got called after the fetching process is finished. Furthermore the method builds a URL by
using the ApiRequestUrlBuilder. The result URL will be used to start the JSON request. 
The result of the JSON will be converted to a RecipeData object and added in the RecipeDataHolder
The other Method (getIngredientsById) gather the information about a specific recipe by using 
the ID. Similar to the first method it will build an URL and add the ingredient data to the RecipeDataHolder

ImageDownloadTask:
This Task was designed as an Async task and also use an eventHandler to notify the parent 
activity if a fetch is completed. The task is using Glide to download Bitmaps from an URL. 
Glide was chosen because it can process redirects which was necessary to get URLs from the 
Food2Fork API. In the PostExecute Method the eventHandler will be called to update the ListViewAdapter.  

