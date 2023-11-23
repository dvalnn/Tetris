# Positions for the assets

PowerPoint saves assets position in cm. In order to render the assets in the right
position, it must be converted to xx and yy coords.

|powerPoint|
| 16:9 |
POWERPOINT_HEIGHT = 19.05cm
POWERPOINT_WIDTH = 33.867cm

|gameWindow|
| 16:9 | GAME_HEIGHT = 720px
GAME_WIDTH = 1280px

Conversion from cm to pixel:
$xx = (GAME_WIDTH * xxCm) / POWERPOINT_WIDTH$
$yy = (GAME_HEIGHT *yyCm ) / POWERPOINT_HEIGHT$

Example of a menu
---_--- <background.<fileType>> ---_---

- <asset1.<fileType>>
  xxCm: <xxCm> cm
  yyCm: <yyCm> cm

- <asset2.<fileType>>
  xxCm: <xxCm> cm
  yyCm: <yyCm> cm

- <pop-up.<fileType>>
  xxCm: <xxCm> cm
  yyCm: <yyCm> cm

The following values represent the position of the assets' _center_ in cm.

Note:

---_--- <image.fileType> ---_--- is the background image

- <button.fileType> is a button

- <popUp.fileType> is a pop-up

---_--- titlescreen.png ---_---

- pressEnter.png
  xxCm: 6.58 cm
  yyCm: 13.79 cm

---_--- mainMenu.png ---_---

- newGame.png
  xxCm: 1.05 cm
  yyCm: 6.25 cm

- leaderboard.png
  xxCm: 1.05 cm
  yyCm: 8.16 cm

- settings.png
  xxCm: 1.05 cm
  yyCm: 6.25 cm

- aboutUs.png
  xxCm: 1.05 cm
  yyCm: 11.97 cm

- exitGame.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

---_--- aboutUs.png ---_---

- returnButton.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

---_--- leaderboard.png ---_---

- returnButton.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

---_--- settings.png ---_---

- changeGameInputs.png
  xxCm: 1.48 cm
  yyCm: 10.68 cm

- plusv1.png
  xxCm: 10.97 cm
  yyCm: 5.48 cm

- minusv1.png
  xxCm: 13.16 cm
  yyCm: 5.48 cm

- plusv2.png
  xxCm: 10.97 cm
  yyCm: 7.88 cm

- minusv2.png
  xxCm: 13.16 cm
  yyCm: 7.88 cm

- settingsPopUp.png
  xxCm: 5.92 cm
  yyCm: 3.61 cm

- returnButton.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

---_--- newGame.png ---_---

- singlePlayer.png
  xxCm: 8.38 cm
  yyCm: 8.84 cm

- multiplayer.png
  xxCm: 18.68 cm
  yyCm: 8.84 cm

- returnButton.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

---_--- multiplayer.png ---_---

- hostGame.png
  xxCm: 8.38 cm
  yyCm: 8.84 cm

- joinGame.png
  xxCm: 18.68 cm
  yyCm: 8.84 cm

- returnButton.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

---_--- multiWaiting.png ---_---

- waitingOppon.png
  xxCm: 8.04 cm
  yyCm: 17.05 cm

- start.png
  xxCm: 8.04 cm
  yyCm: 17.05 cm

- returnButton.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

- playerJoined.png
  xxCm: 8.04 cm
  yyCm: 17.05 cm

- joinedWaiting.png
  xxCm: 8.04 cm
  yyCm: 17.05 cm

---_--- multiCreateSession.png ---_---

- playerName.png
  xxCm: 8.08 cm
  yyCm: 7.16 cm

- roomName.png
  xxCm: 8.08 cm
  yyCm: 9.38 cm

- start.png
  xxCm: 8.04 cm
  yyCm: 17.05 cm

- returnButton.png
  xxCm: 1.05 cm
  yyCm: 17.05 cm

---_--- singleplayerGame.png ---_---

- board.png
  xxCm: 13.53 cm
  yyCm: 2.72 cm

- gameEssentials.png
  xxCm: 13.53 cm // To test!
  yyCm: 2.72 cm // To test!

- countdown.png
  xxCm: 8.53 cm
  yyCm: 4.7 cm

- gamePaused.png
  xxCm: 5.92 cm  
  yyCm: 4.7 cm

- resume.png
  xxCm: 6.33 cm
  yyCm: 7.56 cm

- restart.png
  xxCm: 6.33 cm
  yyCm: 9.26 cm

- exit.png
  xxCm: 6.33 cm
  yyCm: 12.55 cm

- gameoverSingle.png
  xxCm: 5.92 cm
  yyCm: 4.7 cm

- gameOverRetry.png
  xxCm: 10.64 cm
  yyCm: 12.94 cm

- gameOverExit.png
  xxCm: 7.02 cm
  yyCm: 12.94 cm

---_--- multiplayerGame.png ---_---

- countdown.png
  xxCm: 8.53 cm
  yyCm: 4.7 cm

- multiEssentials.png
  xxCm: 16.43 cm
  yyCm: 9.03 cm

- gameOverMulti.png
  xxCm: 5.92 cm
  yyCm: 4.7 cm

- gameOverExit.png
  xxCm: 7.02 cm
  yyCm: 12.94 cm

- gameOverRetry.png
  xxCm: 22.77 cm
  yyCm: 12.94 cm

- waitingForOpponent.png
  xxCm: 5.92 cm
  yyCm: 4.7 cm

- gameOverExit.png
  xxCm: 11.87 cm
  yyCm: 8.77 cm
