Try{
   
    # Invoque command
    $comand="Get-CsUser | Format-Table -Property SamAccountName"
    Write-Host "Comand to execute:"
    Write-Host $comand
    Write-Host "Initiating invocation..."
    Invoke-Expression $comand

   # Success Flag
   If ($?) {
	Write-Host "POWERSHELL COMPLETED SUCCESSFULLY"
   }

} Catch {
   # Exception
   Write-Host $_
}


