# OdometryApp_v10
OdometryApp_v10 is a fully functional edition of the Odometry App - enabling simulation, editing, reordering, and much more!

Odometry Point Selector App for Skystone 2019-2020 FTC season

This app, developed by Mihir Chauhan, is a autonomous path plotting application on Android which enables ease when creating new auto paths on the fly. This app allows you to move many accessories, such as a claw, grabber, etc, as well as base movements such as strafe to position and move to position. When a function is called, it is added to a list which is seen on the right hand side, as well as the many parmaeters for each function being called. If there is any robot movement, a little square robot will move about the field in the real field coordinate plane (144 inch x 144 inch). Coordinates are passed in as inches and start on the bottom right from (0, 0) to the top right, (144, 144). Once created, this app creates a special formated JSON file with all of the movements of accessories and the drivetrain. At the end, you would send it over to the RC (Robot Controller) phone, which has a program to parse the file created by this app, and move the robot to the coordinates you have passed in. You may also load files, edit them, and save them to the original file if any changes are needed to a speific file. This app will simplify the proccess of creating autonomous programs and we can quickly change them before a match in a competition.

The new edition of the app includes more features that help in the creation of the program including: robot simulation, editing, reordering, and more. Once your program is done, the user can choose to watch the robot follow the path and go to the points the user has specified. Reordering is much simpler in this app - just a long press allows you move each cell of the program list view. Editing is also much simpler as the user has to tap on the cell to edit and all of the parameters and function name are prepopulated view with the information of the function you clicked on. 

This app has become much more powerful as functions to be called are based on a JSON formatted text file and static buttons are no longer needed - therefore cleaning up the view and allowing for a bigger field.

Special Thanks to our official beta testers:
  - Avikam Chauhan
  - Mathew Joseph
  - Software Division


© Mihir Chauhan | Innov8rz FTC Team 11039 | All Rights Reserved 2020
