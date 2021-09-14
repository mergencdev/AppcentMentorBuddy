# Appcent Mentor Buddy

The project we developed with the help of my mentor Alaa Eddin Albarghoth during my internship.

*This project represents a mobile cloud based gallery application.*

![Logo](https://i.imgur.com/xFc0aib.png)

**Mentor**: [Alaa Eddin Albarghoth](https://github.com/AlaaEddinAlbarghoth)\
Mentee: [Mehmet Emin Ergenç](https://github.com/mergencdev)
## Authors and Acknowledgments

Of course, I am grateful to my mentor [Alaa Eddin Albarghoth](https://github.com/AlaaEddinAlbarghoth) and the entire [Appcent](https://www.appcent.mobi/) team.

  
## Used Technologies

Firebase, Cloud Firestore, Firebase Realtime Database, Firebase Storage, Firebase Authentication


  
## Project Task Document

[Appcent Mentor Buddy Task Document — By Alaa Eddin Albarghoth](https://drive.google.com/file/d/1vDZVxjTbcMM-hv4VTSXMFzpcGOyrB2L_/view?usp=sharing)

  
## Run it

Clone the project

```bash
  git clone https://github.com/mergencdev/AppcentMentorBuddy
```

Open in Android Studio and run.

## Screenshots

![Splash Screen](https://i.imgur.com/mT9dr8r.png)     ![Feed](https://i.imgur.com/I81Q1pz.jpg)

![Trash](https://i.imgur.com/UVKwByZ.jpg)     ![Upload](https://i.imgur.com/trNErjx.jpg)

### Desired features of the project

- Create a Tabbed Application:

        Tab 1: (Feed)
        1. Show news feed in a grid of 2 columns (grid item should contain image, title and description).
        2. A button to switch between fixed grid size and automatic height based on the image's aspect ratio.
        Note: Please refer to the following images for a better understanding of fixed and automatic grid sizing
        3. Cache all the images into Disk after downloading to be used offline.
        4. Make a custom search bar that looks like the following:
        Note: don't forget to show/hide the progress loader in the search bar.
        5. Tap on the camera icon to take a picture from the camera and insert it into the grid. Note: Once you
        add the item to the grid, show a local system notification. 
        6. Long press gestures on the grid item rearrange the positions of the item.
        7. Tap on the grid item to delete with a confirmation bottom sheet.
        8. If there are no items in the grid, show a view like the following:
        9. Share button on the grid item to share the image to WhatsApp, Facebook, etc.
        10. Add a timer to check every 10 minutes; if there are more than ten feeds, remove all the extras.
        11. All the deleted feeds should go into Tab 2 as a list.
    
        Tab 2: (Trash)
        1. A table view is displaying all the deleted feeds with the date and time of deletion.
        2. Add the 'swipe to delete' action to delete the feed permanently.
        
- Instructions for the test:
        
        1. Follow MVVM architecture and use Kotlin language.
        2. Ensure the code is clean, understandable with comments wherever required, reusable, and bugfree.
        3. You can use any third-party libraries or APIs to get dummy images or add a JSON file with image
        URLs in the project


## Resources I used during development (Many thanks to those who contributed :heart:)

https://developer.android.com/topic/libraries/view-binding

https://developer.android.com/guide/navigation/navigation-getting-started#kts

https://developer.android.com/guide/topics/ui/layout/cardview

https://github.com/hdodenhof/CircleImageView

https://stackoverflow.com/questions/40057798/firebase-token-authentication-error

https://firebase.google.com/docs/auth/android/custom-auth

https://firebase.google.com/docs/storage/android/upload-files#kotlin+ktx

https://stackoverflow.com/questions/49793203/firebase-storage-error-error-getting-token

https://stackoverflow.com/questions/54527868/how-to-get-the-date-and-time-when-uploading-image-to-firebase

https://developer.android.com/topic/libraries/view-binding#kotlin

https://www.geeksforgeeks.org/view-binding-with-fragments-in-android-jetpack/

https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the

https://www.geeksforgeeks.org/view-binding-with-fragments-in-android-jetpack/

https://square.github.io/picasso/

https://github.com/bumptech/glide

https://stackoverflow.com/questions/51344714/how-to-create-option-menu-in-fragment/51344762

https://github.com/MikeOrtiz/TouchImageView

https://www.tutorialspoint.com/how-to-set-dialog-to-show-with-full-screen-in-android-using-kotlin

https://developer.android.com/guide/topics/ui/dialogs#AlertDialog

https://developer.android.com/training/sharing/send

https://stackoverflow.com/questions/62499608/kotlin-how-to-pass-data-from-recyclerview-in-fragment-to-another-fragment-using

https://stackoverflow.com/questions/53694594/changing-visibility-of-a-recyclerview-item-changes-other-items-visibility

https://developer.android.com/training/notify-user/build-notification

https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked

https://firebase.google.com/docs/firestore/query-data/get-data#kotlin+ktx_4

https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2

https://stackoverflow.com/questions/54144543/firestore-google-cloud-timestamp-parsing?rq=1

https://stackoverflow.com/questions/49488312/androidstudio-import-svg-with-error-mask-is-not-supported

https://svg2vector.com/

https://developer.android.com/studio/write/vector-asset-studio#svg

https://stackoverflow.com/questions/42595335/how-to-implement-android-splash-screen-using-svg-instead-of-png

https://developer.android.com/training/dependency-injection/hilt-android#groovy

## Colors
| Hex                                                              |
------------------------------------------------------------------ |
| ![#1E2C3F](https://via.placeholder.com/10/1E2C3F?text=+) #1E2C3F |
| ![#FF7A00](https://via.placeholder.com/10/FF7A00?text=+) #FF7A00 |
| ![#BFC0C2](https://via.placeholder.com/10/BFC0C2?text=+) #BFC0C2 |
| ![#D5D5D5](https://via.placeholder.com/10/D5D5D5?text=+) #D5D5D5 |


## Support and Feedback

If you want to support me or give any feedback, please contact me at mergencdev@gmail.com.

[![Tweet](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fmergencdev%2FAppcentMentorBuddy)](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fmergencdev%2FAppcentMentorBuddy)

## License

[GNU General Public License v3.0](https://choosealicense.com/licenses/gpl-3.0/)

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
