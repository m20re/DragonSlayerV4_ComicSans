# DragonSlayerV4_ComicSans
Capstone Project for UNO class CSCI4970.

## Prerequisites:
- JDK21
- Apache Maven

## How to use:
Ensure that Maven is installed within your environmental variables.

Run the command: `mvn clean package -DskipTests -Pdev`
- This will create a standalone `.exe` for testing.
- It can be found within `{installationDirectory}\dragonslayer\target\distributable\Dragonslayer`

## Changelog:
#### Version 4.0.0
###### Major Updates
- Updated from Java 17 --> Java 21
- Updated from JavaFX 18 --> JavaFX 21.0.8

###### Additions
- Included `Add` button on the Title tab for the customer.
- Added `Delete` button on the Title tab for the customer as well.

###### Bug Fixes
- Fixed ghost title issue, where titles would randomly pop-up.
- Fixed flagging issues

#### Version 4.0.1
##### Additions
- Added deletion confirmation message for customers within the title tab.
- Highlights new customers with no orders.

##### Bug Fixes
- Fixed issue where sort by title was slow.

#### Version 4.1.0
###### Additions
- Included `DerbyDB` folder button
- Changed `DerbyDB` folder location to reflect APP_Image distribution
- Added JUnit 5 + Simple Testing suite

###### Bug fixes
- Fixed title overflowing into other fields

#### Version 4.2.0
###### Additions
- Included Sort by Last flagged column
- Previous Customers Tab, along with associated junction table
- Added refresh button to refresh Database connection
- Changed icon
- Added save test in hopes of squashing the bug
- Added "Pending" stats in the flagged column
- Created a pop-up to save to two different stores
- Created "View-Only" mode.
- Included Show Previous Box to show titles with older customers

###### Bug fixes
- Added potential fix for exports not saving correctly
- Fixed bug where the customer wasn't being added to a title
- Fixed performance searching performance issues
- Fixed edge-case where names with hyphens will break the SQL query
- Fixed titles not displaying after being created.
