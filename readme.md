# Return To Wonderland
Source code for Return To Wonderland and Editor.

## How to Use

1. Download and install Blitz3D v1.83. DO NOT INSTALL ANY LATER VERSION. The game will crash after sometime with later Blitz versions.
You can download it from here: https://drive.google.com/file/d/1KoT-CwoiIzV27WGsPyts_jKiUcTAOcvj/view?usp=sharing
Extract it to a clean directory. The zip file also contains the required userlibs for the code to run. You can read readme.txt in it for further instructions (e.g how to install dgvoodoo2) and licensing notes.

2. Clone this repo. Copy the res# files, ldr# files, Wonderland.exe and Custom directories from your RTW full version copy into the repo.

3. (Optional) Set up Visual Studio Code for QoL. Download and install it from Microsoft website, and download Blitz3D Language support extension. Create a file .vscode/settings.json: (put your Blitz3D path in place of `D:\\Blitz3D v1.83`)
```JSON
{
    "blitz3d.installation.BlitzPath": "D:\\Blitz3D v1.83",
    "terminal.integrated.env.windows": {
          "PATH": "${env:PATH};D:\\Blitz3D v1.83\\bin",
          "BLITZPATH": "D:\\Blitz3D v1.83"
    },
}
```

4. Now you are ready to compile and run game.bb (the main game) and editor.bb (editor). To run via VSCode, open either of these files and in Run menu click "Start Debugging" to debug or "Run Without Debugging" to start without debugger. You can also use Blitz via command line, just use `blitzcc` command via VSCode terminal for various Blitz tasks. Run `blitzcc -h` in your VSCode terminal for more details.


## Before You Start
Return To Wonderland and its source code are copyrighed by Midnight Synergy.
Please read the license file before continuing to work on this source code.

Additionally, read the Modding Rules in the Woderland Open Source forum before continuing: https://pcpuzzle.com/forum/viewtopic.php?t=27841

And, please do NOT use GenAI for any modded work, except for maybe IDE auto-completions and assisting with some time-consuming menial coding or code formatting tasks. Not to mention since this is legacy Blitz code, AI models are prone to break and hallucinate here.
