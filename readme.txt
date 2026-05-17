// open 2 separate powershells , one for server , other for client 

Server: 
cd "E:\#1 University\#1 3rd year CSE\#1 Second Term\Parallel\Distributed Project"
& 'C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe' -d GroceryServer\out GroceryServer\src\server\Server.java GroceryServer\src\server\ServerHandler.java
& 'C:\Program Files\Java\jdk1.8.0_202\bin\java.exe' -cp GroceryServer\out server.Server



Client:
cd "E:\#1 University\#1 3rd year CSE\#1 Second Term\Parallel\Distributed Project"
& 'C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe' -cp "GroceryClient\lib\*" -d GroceryClient\out `
  GroceryClient\src\client\LoginFrame.java `
  GroceryClient\src\client\ShopFrame.java `
  GroceryClient\src\client\ui\UiTheme.java `
  GroceryClient\src\client\ui\UiKit.java `
  GroceryClient\src\client\ui\CheckoutDialog.java
& 'C:\Program Files\Java\jdk1.8.0_202\bin\java.exe' -cp "GroceryClient\out;GroceryClient\lib\*" client.LoginFrame



user: Youssef_Ayman
Pass: Youssef_33263053
