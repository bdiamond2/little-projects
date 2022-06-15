Attribute VB_Name = "SheetFunctions"
Public Declare PtrSafe Sub Sleep Lib "kernel32" (ByVal Milliseconds As LongPtr)

Sub Plot(x, y, floor, color)
    Dim address As String
    Dim rowIndex, colIndex As Integer
    
    x = x / 2
    y = y / 2
    
    rowIndex = WorksheetFunction.Max(-y + floor, 1)
    colIndex = WorksheetFunction.Max(x, 1)
    
    rowIndex = WorksheetFunction.Min(rowIndex, 1000)
    colIndex = WorksheetFunction.Min(colIndex, 1000)
    
    Call PlotCore(rowIndex, colIndex, color)
End Sub

Sub PlotCore(rowIndex, colIndex, color)
    Dim startRowIndex, endRowIndex As Integer
    Dim startColIndex, endColIndex As Integer
    Dim i, j As Integer
    Dim address As String

    startRowIndex = WorksheetFunction.Max(rowIndex - 0, 1)
    endRowIndex = rowIndex + 0

    startColIndex = WorksheetFunction.Max(colIndex - 0, 1)
    endColIndex = colIndex + 0

    For i = startRowIndex To endRowIndex
        For j = startColIndex To endColIndex
            address = Cells(i, j).address()
            Range(address).Interior.color = color
        Next j
    Next i
End Sub

Sub Button1_Click()
    Call CleanSheet
    'Call Projectile.Projectile2
    'Call Gravity.Gravity
    Call NBodyGravity.Main
End Sub

Sub CleanSheet()
    Cells.Clear
    Cells.Interior.color = RGB(255, 255, 255)
    Rows.RowHeight = 2 * (3 / 4) '1px = 3/4 pt
    Columns.ColumnWidth = 2 * (1 / 12) '1px = 1/12 char
End Sub
