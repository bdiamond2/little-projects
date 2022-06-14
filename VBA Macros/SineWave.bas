Attribute VB_Name = "SineWave"
Sub SineWave()
    Dim address As String
    Dim x, y, xMax As Double
    Dim rowIndex, colIndex As Integer
    Dim pi As Double
    
    pi = WorksheetFunction.pi()
    xMax = 500
    
    For x = 1 To xMax
        'manipulate angle so it completes one period
        y = 100 * Sin(x * (2 * pi / xMax))
        
        'coordinate transform
        rowIndex = WorksheetFunction.Max(-y + 150, 1)
        colIndex = WorksheetFunction.Max(x, 1)
        
        address = Cells(rowIndex, colIndex).address()
        Range(address).Interior.color = RGB(0, 0, 0)
    Next x
End Sub
