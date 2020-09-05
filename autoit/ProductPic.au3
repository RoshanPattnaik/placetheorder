$winHandle =WinGetHandle("Open")
WinActivate($winHandle)
ControlSetText("Open","","[CLASS:Edit;INSTANCE:1]","E:\2020-Learnings\eclipse-workspace\MaaaksfinAutomation\autoit\tomato.jpg")
ControlClick("Open","","[CLASS:Button;INSTANCE:1]")