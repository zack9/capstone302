Health Diary Read Me
Health Diary version 3.4

Created by Kenneth Lewis, Dylan Schumacher, Alex Tulic, Zack Watson, Taylor Beer

In Order to run HealthDiary the apk must first be installed onto the phone.  The main way this is done is to load the application into Android Studio and use a mobile
device instead of an emulator.  This was done during testing.  Testers devices were connected to a developers laptop and the current app version was installed onto their
device.

Once the applicaiton opens it is nessesary to firstly create the SQLite databases.  Unfortunetly during production it was not possible to gain access to QUTs database which
hindered development. To load the SQLite database please select the settings option (top right) and click load databases.  The user should be toasted to say the databases
have been created.  In this creation process two text files are read into the databases, a Users text file (used for testing only) and LookupFood.txt which holds all the 
information for each food item stored in this order (ID, NAME, ENERGRY(kj), CALORIES, PROTEIN, FAT, SUGAR, SODIUM).

If the current user isn't registered through the Users txt file, the user will firstly need to register.  Click the register and add the required information and hit save.
Once this is done the newly created user should be able to login.  Once logged in the user will be toasted to enter more information about themselves.  Please go to the top
right and click settings.  From here please set the User Goals, and User Traits.  Once this is done, the app will start updating 'At A Glance Correctly'.

To add a food to the diary click the 'Eat' Button located on the home page.  This EatActivity will display the food eaten each day, week, month.  Click the 'Add Entry' button
to add food to the diary.  Once in the search window start typing an items name then once found hit the search button. **There is a bug here, see Bug report below**.  After 
the item has been added, hit the save button and the newly added item will appear in the EatActivity.  If the user presses the 'Home' button (top right), and navigates back
to the main page, they will notice the 'At A Glance' has been updated with their new food entry.

KNOWN BUGS

Major
-Search Food Food Items
 x This unfortunetly is the major but present in the application. It currently only enables users to search for food once.  While the app doesn't nessesarily crash, display
	errors occur and incorrect food items can be added as a result.
	To avoid this the user MUST: First open the activity -> start typing in a letter then once food is located simply press the food item then hit the magnifing glass.
				     This can be repeated by for example searching for 600ml coke type 6 then add coke, backspace the 6 and type P and add an item and hit 
				     search and both items will be displayed.
	This is currently the only way to add food entries without errors.
-Database Delete
 x This works but can cause a crash, currently set to delete user traits table

Minor
-Some display errors are present throughout the app

WHAT WE AIMED FOR BUT COULDNT IMPLEMENT

- We wanted to add further user reporting for users to see how much sodium, protein they consumed.
- Also had aimed to implement a BMI calculator, and goals based on health industry averages etc
- Implementation of importing a picture for an avatar
- Change the theme of the application (colours etc)
- Achivements, if user achived their goals for the day it would save an achivement ie steps > step goal = SirWalksAlot Achivement etc
- Smart watch intergration
- More gamification elements
- Social media aspect to post achivements

Minor