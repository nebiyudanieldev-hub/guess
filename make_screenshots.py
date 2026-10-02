import subprocess
import os

os.makedirs('amazon_assets', exist_ok=True)

# Screenshot 1: Home & Categories
cmd1 = [
    "convert", "-size", "1080x1920", "xc:#0F172A",
    "-fill", "#6366F1", "-draw", "rectangle 0,0 1080,320",
    "-fill", "white", "-pointsize", "72", "-font", "DejaVu-Sans-Bold", "-gravity", "North", "-annotate", "+0+80", "GUESS!",
    "-pointsize", "36", "-font", "DejaVu-Sans", "-annotate", "+0+190", "Visual & Audio Trivia Challenge",
    "-fill", "#1E293B", "-draw", "roundrectangle 80,380 1000,560 30,30",
    "-fill", "#F59E0B", "-pointsize", "44", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+120+410", "🔥 Daily Streak: 5 Days",
    "-fill", "#94A3B8", "-pointsize", "32", "-font", "DejaVu-Sans", "-annotate", "+120+480", "Play daily to earn 2X multiplier points!",
    "-fill", "#1E293B", "-draw", "roundrectangle 80,620 500,860 30,30",
    "-fill", "white", "-pointsize", "48", "-font", "DejaVu-Sans-Bold", "-annotate", "+120+670", "🌍 Countries",
    "-fill", "#94A3B8", "-pointsize", "28", "-font", "DejaVu-Sans", "-annotate", "+120+740", "Flags & Capitals",
    "-fill", "#1E293B", "-draw", "roundrectangle 580,620 1000,860 30,30",
    "-fill", "white", "-pointsize", "48", "-font", "DejaVu-Sans-Bold", "-annotate", "+620+670", "🚗 Cars",
    "-fill", "#94A3B8", "-pointsize", "28", "-font", "DejaVu-Sans", "-annotate", "+620+740", "Supercars & Badges",
    "-fill", "#1E293B", "-draw", "roundrectangle 80,920 500,1160 30,30",
    "-fill", "white", "-pointsize", "48", "-font", "DejaVu-Sans-Bold", "-annotate", "+120+970", "⚽ Football",
    "-fill", "#94A3B8", "-pointsize", "28", "-font", "DejaVu-Sans", "-annotate", "+120+1040", "Clubs & Legends",
    "-fill", "#1E293B", "-draw", "roundrectangle 580,920 1000,1160 30,30",
    "-fill", "white", "-pointsize", "48", "-font", "DejaVu-Sans-Bold", "-annotate", "+620+970", "💻 Tech",
    "-fill", "#94A3B8", "-pointsize", "28", "-font", "DejaVu-Sans", "-annotate", "+620+1040", "Innovations & Logos",
    "-fill", "#10B981", "-draw", "roundrectangle 120,1650 960,1800 40,40",
    "-fill", "white", "-pointsize", "52", "-font", "DejaVu-Sans-Bold", "-gravity", "Center", "-annotate", "+0+780", "QUICK PLAY NOW",
    "amazon_assets/screenshot_1_home.png"
]
subprocess.run(cmd1, check=True)

# Screenshot 2: Gameplay
cmd2 = [
    "convert", "-size", "1080x1920", "xc:#0F172A",
    "-fill", "#4F46E5", "-draw", "rectangle 0,0 1080,240",
    "-fill", "white", "-pointsize", "44", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+80+80", "Question 4 / 10",
    "-fill", "#F59E0B", "-pointsize", "44", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthEast", "-annotate", "+80+80", "Score: 350",
    "-fill", "#EF4444", "-draw", "roundrectangle 440,290 640,430 70,70",
    "-fill", "white", "-pointsize", "64", "-font", "DejaVu-Sans-Bold", "-gravity", "North", "-annotate", "+0+330", "18s",
    "-fill", "#1E293B", "-draw", "roundrectangle 80,480 1000,980 40,40",
    "-fill", "white", "-pointsize", "140", "-gravity", "North", "-annotate", "+0+540", "🇪🇹",
    "-fill", "white", "-pointsize", "46", "-font", "DejaVu-Sans-Bold", "-annotate", "+0+740", "Which country does this flag belong to?",
    "-fill", "#334155", "-draw", "roundrectangle 100,1040 980,1180 30,30",
    "-fill", "white", "-pointsize", "42", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+160+1090", "A. Kenya",
    "-fill", "#10B981", "-draw", "roundrectangle 100,1220 980,1360 30,30",
    "-fill", "white", "-pointsize", "42", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+160+1270", "B. Ethiopia  ✓",
    "-fill", "#334155", "-draw", "roundrectangle 100,1400 980,1540 30,30",
    "-fill", "white", "-pointsize", "42", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+160+1450", "C. Ghana",
    "-fill", "#334155", "-draw", "roundrectangle 100,1580 980,1720 30,30",
    "-fill", "white", "-pointsize", "42", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+160+1630", "D. Nigeria",
    "amazon_assets/screenshot_2_gameplay.png"
]
subprocess.run(cmd2, check=True)

# Screenshot 3: Leaderboard & Stats
cmd3 = [
    "convert", "-size", "1080x1920", "xc:#0F172A",
    "-fill", "#F59E0B", "-draw", "rectangle 0,0 1080,260",
    "-fill", "#0F172A", "-pointsize", "64", "-font", "DejaVu-Sans-Bold", "-gravity", "North", "-annotate", "+0+80", "LEADERBOARD",
    "-fill", "#1E293B", "-pointsize", "34", "-font", "DejaVu-Sans", "-annotate", "+0+170", "Top Guess Masters",
    "-fill", "#1E293B", "-draw", "roundrectangle 80,320 1000,520 30,30",
    "-fill", "#FBBF24", "-pointsize", "60", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+140+390", "🥇 1st Place",
    "-fill", "white", "-pointsize", "40", "-font", "DejaVu-Sans", "-annotate", "+520+405", "QuizMaster99",
    "-fill", "#38BDF8", "-pointsize", "44", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthEast", "-annotate", "+140+395", "1,850 pts",
    "-fill", "#1E293B", "-draw", "roundrectangle 80,560 1000,760 30,30",
    "-fill", "#E2E8F0", "-pointsize", "60", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+140+630", "🥈 2nd Place",
    "-fill", "white", "-pointsize", "40", "-font", "DejaVu-Sans", "-annotate", "+520+645", "BrainRunner",
    "-fill", "#38BDF8", "-pointsize", "44", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthEast", "-annotate", "+140+635", "1,620 pts",
    "-fill", "#1E293B", "-draw", "roundrectangle 80,800 1000,1000 30,30",
    "-fill", "#F97316", "-pointsize", "60", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthWest", "-annotate", "+140+870", "🥉 3rd Place",
    "-fill", "white", "-pointsize", "40", "-font", "DejaVu-Sans", "-annotate", "+520+885", "GuessAce",
    "-fill", "#38BDF8", "-pointsize", "44", "-font", "DejaVu-Sans-Bold", "-gravity", "NorthEast", "-annotate", "+140+875", "1,490 pts",
    "-fill", "#4F46E5", "-draw", "roundrectangle 80,1080 1000,1400 40,40",
    "-fill", "white", "-pointsize", "52", "-font", "DejaVu-Sans-Bold", "-gravity", "North", "-annotate", "+0+1120", "Your Rank: #4",
    "-fill", "#A5B4FC", "-pointsize", "36", "-font", "DejaVu-Sans", "-annotate", "+0+1200", "Current Score: 1,380 pts",
    "-fill", "#FDE047", "-pointsize", "38", "-font", "DejaVu-Sans-Bold", "-annotate", "+0+1280", "Streak Bonus: +150 pts!",
    "-fill", "#6366F1", "-draw", "roundrectangle 120,1650 960,1800 40,40",
    "-fill", "white", "-pointsize", "50", "-font", "DejaVu-Sans-Bold", "-gravity", "Center", "-annotate", "+0+780", "PLAY ANOTHER ROUND",
    "amazon_assets/screenshot_3_leaderboard.png"
]
subprocess.run(cmd3, check=True)
print("ALL SCREENSHOTS CREATED SUCCESSFULLY!")
