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
