PowerPoint saves assets position in cm. In order to render the asset in the right
position, it must be converted to xx and yy coords.


   ----------
  |powerPoint|
  |   16:9   | POWERPOINT_HEIGHT = 19.05cm
  |          |
   ----------
    POWERPOINT_WIDTH = 33.867cm

   ----------
  |gameWindow|
  |   16:9   | GAME_HEIGHT = 720px
  |          |
   ----------
     GAME_WIDTH = 1280px


Conversion from cm to pixel:


xx = (GAME_WIDTH * xxCm) / POWERPOINT_WIDTH
yy = (GAME_HEIGHT *yyCm ) / POWERPOINT_HEIGHT  

The following values represent the position of the assets' *center* in cm.

- TitleScreen
  * pressEnter.png
    xxCm: 6.58  cm
    yyCm: 13.79 cm

---*--- MainMenu ---*---
  * newGame.png
      xxCm: 1.05 cm
      yyCm: 6.25 cm
  * leaderboard.png
      xxCm: 1.05 cm
      yyCm: 8.16 cm
  * settings.png
      xxCm: 1.05 cm
      yyCm: 6.25 cm
  * aboutUs.png
      xxCm: 1.05 cm
      yyCm: 11.97 cm
  * exitGame.png
      xxCm: 1.05  cm
      yyCm: 17.05 cm

---*--- AboutUs ---*---
  * returnButton.png
      xxCm: 1.05 cm
      yyCm: 17.05 cm

---*--- Leaderboard ---*---
  * returnButton.png
      xxCm: 1.05 cm
      yyCm: 17.05 cm

---*--- Settings ---*---
  * ChangeGameInputs
      xxCm: 1.48 cm
      yyCm: 10.68 cm

  * IncreaseMusicVol
      xxCm: 10.97 cm
      yyCm: 5.48 cm

  * DecreaseMusicVol
      xxCm: 13.16 cm
      yyCm: 5.48 cm

  * IncreaseEffectsVol
      xxCm: 10.97 cm
      yyCm: 7.88 cm

  * DecreaseEffectsVol
      xxCm: 13.16 cm
      yyCm: 7.88 cm

  * settingsPopUp.png
      xxCm: 5.92 cm
      yyCm: 3.61 cm

  * returnButton.png
      xxCm: 1.05 cm
      yyCm: 17.05 cm

---*--- NewGame ---*---
  * singlePlayer.png
      xxCm: 8.38 cm
      yyCm: 8.84 cm

  * multiplayer.png
      xxCm: 18.68 cm
      yyCm: 8.84 cm

  * returnButton.png
      xxCm: 1.05 cm
      yyCm: 17.05 cm

    
    
    