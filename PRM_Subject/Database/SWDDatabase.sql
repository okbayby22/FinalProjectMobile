CREATE TABLE Customer (customerId int(10) NOT NULL AUTO_INCREMENT, customerName varchar(100) NOT NULL, customerEmail varchar(255) NOT NULL, customerAddress varchar(255), customerPhone varchar(13) NOT NULL, customerUsername varchar(50) NOT NULL UNIQUE, customerPassword varchar(32) NOT NULL, customerGender int(1) NOT NULL, customerBalance double NOT NULL, customerPoint int(10) NOT NULL, customerStatus int(1) NOT NULL, customerAvatar varchar(255), PRIMARY KEY (customerId));
CREATE TABLE Reservation (reservationId int(10) NOT NULL AUTO_INCREMENT, customerId int(10) NOT NULL, reservationTime time NOT NULL, reservationStatus int(1) NOT NULL, staffId int(10) NOT NULL, deskId int(10) NOT NULL, NumberOfPeople int(2) NOT NULL, discountId int(10) NOT NULL, PRIMARY KEY (reservationId));
CREATE TABLE Desk (deskId int(10) NOT NULL AUTO_INCREMENT, seat int(10) NOT NULL, deskStatus int(1) NOT NULL, PRIMARY KEY (deskId));
CREATE TABLE Staff (staffId int(10) NOT NULL AUTO_INCREMENT, staffName varchar(100) NOT NULL, staffEmail varchar(255) NOT NULL, staffAddress varchar(255), staffPhone varchar(13) NOT NULL, staffUsername varchar(50) NOT NULL UNIQUE, staffPassword varchar(32) NOT NULL, staffGender int(1) NOT NULL, role int(1) NOT NULL, staffStatus int(1) NOT NULL, staffImage varchar(255) NOT NULL, PRIMARY KEY (staffId));
CREATE TABLE WeeklyTimeTable (staffId int(10) NOT NULL, shift int(1) NOT NULL, workingDate date NOT NULL, attendanceStatus int(1) NOT NULL);
CREATE TABLE Menu (menuId int(10) NOT NULL AUTO_INCREMENT, menuName varchar(200) NOT NULL, menuDate date NOT NULL, staffId int(10) NOT NULL, PRIMARY KEY (menuId));
CREATE TABLE Food (foodId int(10) NOT NULL AUTO_INCREMENT, foodName varchar(100) NOT NULL, foodImage varchar(255) NOT NULL, menuId int(10) NOT NULL, PRIMARY KEY (foodId));
CREATE TABLE Discount (discountId int(10) NOT NULL AUTO_INCREMENT, discountPercent int(10) NOT NULL, discountStatus int(1) NOT NULL, discountPoint int(10), discountDescription varchar(255), staffId int(10) NOT NULL, PRIMARY KEY (discountId));
CREATE TABLE Discount_Inventory (customerId int(10) NOT NULL, discountId int(10) NOT NULL, discountCode varchar(12), PRIMARY KEY (customerId, discountId));
ALTER TABLE Reservation ADD INDEX FKReservatio206160 (customerId), ADD CONSTRAINT FKReservatio206160 FOREIGN KEY (customerId) REFERENCES Customer (customerId);
ALTER TABLE Reservation ADD INDEX FKReservatio845390 (staffId), ADD CONSTRAINT FKReservatio845390 FOREIGN KEY (staffId) REFERENCES Staff (staffId);
ALTER TABLE WeeklyTimeTable ADD INDEX FKWeeklyTime334827 (staffId), ADD CONSTRAINT FKWeeklyTime334827 FOREIGN KEY (staffId) REFERENCES Staff (staffId);
ALTER TABLE Food ADD INDEX FKFood114705 (menuId), ADD CONSTRAINT FKFood114705 FOREIGN KEY (menuId) REFERENCES Menu (menuId);
ALTER TABLE Menu ADD INDEX FKMenu452923 (staffId), ADD CONSTRAINT FKMenu452923 FOREIGN KEY (staffId) REFERENCES Staff (staffId);
ALTER TABLE Reservation ADD INDEX FKReservatio391568 (deskId), ADD CONSTRAINT FKReservatio391568 FOREIGN KEY (deskId) REFERENCES Desk (deskId);
ALTER TABLE Reservation ADD INDEX FKReservatio384113 (discountId), ADD CONSTRAINT FKReservatio384113 FOREIGN KEY (discountId) REFERENCES Discount (discountId);
ALTER TABLE Discount_Inventory ADD INDEX FKDiscount_I336263 (customerId), ADD CONSTRAINT FKDiscount_I336263 FOREIGN KEY (customerId) REFERENCES Customer (customerId);
ALTER TABLE Discount_Inventory ADD INDEX FKDiscount_I514216 (discountId), ADD CONSTRAINT FKDiscount_I514216 FOREIGN KEY (discountId) REFERENCES Discount (discountId);
ALTER TABLE Discount ADD INDEX FKDiscount886956 (staffId), ADD CONSTRAINT FKDiscount886956 FOREIGN KEY (staffId) REFERENCES Staff (staffId);
