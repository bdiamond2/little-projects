Attribute VB_Name = "Spiral"
Sub Spiral()
'
' Spiral Macro
    
    Dim origX, origY As Integer 'origin
    Dim x, y, t, tMax As Double
    Dim rowIndex, colIndex As Integer
    Dim amp As Double
    Dim periods As Integer
    Dim angleFactor As Double
    Dim pi As Double
    Dim address As String
    
    pi = WorksheetFunction.pi()
    origX = 200
    origY = 200
    tMax = 2000
    periods = 3
    angleFactor = 2 * pi / tMax
    
    For t = 1 To tMax
        amp = t / 10
        x = amp * Cos(t * periods * angleFactor)
        y = amp * Sin(t * periods * angleFactor)
        
        rowIndex = WorksheetFunction.Max(-y + origY, 1)
        colIndex = WorksheetFunction.Max(x + origX, 1)
        
        address = Cells(rowIndex, colIndex).address()
        Range(address).Interior.color = RGB(0, 0, 0)
    Next t
'
End Sub
