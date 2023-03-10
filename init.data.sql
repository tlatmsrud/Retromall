insert into tb_category(category_name, parent, id) values ('Play Station', null, 'CT_PS');
insert into tb_category(category_name, parent, id) values ('PS4', 'Play Station', 'CT_PS4');
insert into tb_category(category_name, parent, id) values ('PS3', 'Play Station', 'CT_PS3');
insert into tb_category(category_name, parent, id) values ('PS5', 'Play Station', 'CT_PS5');
insert into tb_category(category_name, parent, id) values ('PS2', 'Play Station', 'CT_PS2');
insert into tb_category(category_name, parent, id) values ('Xbox', null, 'CT_XBOX');
insert into tb_category(category_name, parent, id) values ('Xbox 360 Series', 'Xbox', 'CT_XBOX360');
insert into tb_category(category_name, parent, id) values ('Xbox One Series', 'Xbox', 'CT_XBOXOne');
insert into tb_category(category_name, parent, id) values ('Xbox(2001)', 'Xbox', 'CT_XBOX01');
insert into tb_category(category_name, parent, id) values ('Xbox 360(2005)', 'Xbox 360 Series', 'CT_XBOX36001');
insert into tb_category(category_name, parent, id) values ('Xbox 360 S(2010)', 'Xbox 360 Series', 'CT_XBOX36002');
insert into tb_category(category_name, parent, id) values ('Xbox 360 E(2013)', 'Xbox 360 Series', 'CT_XBOX36003');
insert into tb_category(category_name, parent, id) values ('Xbox One(2013)', 'Xbox One Series', 'CT_XBOXOne01');
insert into tb_category(category_name, parent, id) values ('Xbox One X(2017)', 'Xbox One Series', 'CT_XBOXOne02');
insert into tb_category(category_name, parent, id) values ('Xbox One All Digital(2019)', 'Xbox One Series', 'CT_XBOXOne03');
insert into tb_category(category_name, parent, id) values ('Xbox Series S(2020)', 'Xbox', 'CT_XBOXS');
insert into tb_category(category_name, parent, id) values ('Xbox Series X(2020)', 'Xbox', 'CT_XBOXX');
insert into tb_member(member_id, oauth_type, oauth_id, email, nickname) values (1000, 'KAKAO', '00200000', 'test@gmail.com', 'janchen');