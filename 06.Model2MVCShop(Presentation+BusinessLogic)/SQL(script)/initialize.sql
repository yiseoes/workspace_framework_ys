
DROP TABLE transaction;
DROP TABLE product;
DROP TABLE users;

DROP SEQUENCE seq_product_prod_no;
DROP SEQUENCE seq_transaction_tran_no;


CREATE SEQUENCE seq_product_prod_no		 	INCREMENT BY 1 START WITH 10000;
CREATE SEQUENCE seq_transaction_tran_no	INCREMENT BY 1 START WITH 10000;


CREATE TABLE users ( 
	user_id 			VARCHAR2(20)	NOT NULL,
	user_name 	VARCHAR2(50)	NOT NULL,
	password 		VARCHAR2(10)	NOT NULL,
	role 					VARCHAR2(5) 		DEFAULT 'user',
	ssn 					VARCHAR2(13),
	cell_phone 		VARCHAR2(14),
	addr 				VARCHAR2(100),
	email 				VARCHAR2(50),
	reg_date 		DATE,
	PRIMARY KEY(user_id)
);


CREATE TABLE product ( 
	prod_no 					NUMBER 				NOT NULL,
	prod_name 				VARCHAR2(100) 	NOT NULL,
	prod_detail 				VARCHAR2(200),
	manufacture_day   VARCHAR2(8),
	price 							NUMBER(10),
	image_file 					VARCHAR2(100),
	reg_date 					DATE,
	pro_tran_code 				VARCHAR2(20), 
	PRIMARY KEY(prod_no)
);

CREATE TABLE transaction ( 
	tran_no 					NUMBER 			NOT NULL,
	prod_no 					NUMBER(16)		NOT NULL REFERENCES product(prod_no),
	buyer_id 				VARCHAR2(20)	NOT NULL REFERENCES users(user_id),
	payment_option		CHAR(3),
	receiver_name 		VARCHAR2(20),
	receiver_phone		VARCHAR2(14),
	demailaddr 			VARCHAR2(100),
	dlvy_request 			VARCHAR2(100),
	tran_status_code	CHAR(3),
	order_data 			DATE,
	dlvy_date 				DATE,
	PRIMARY KEY(tran_no)
);


INSERT 
INTO users ( user_id, user_name, password, role, ssn, cell_phone, addr, email, reg_date ) 
VALUES ( 'admin', 'admin', '1234', 'admin', NULL, NULL, '서울시 서초구', 'admin@mvc.com',to_date('2012/01/14 10:48:43', 'YYYY/MM/DD HH24:MI:SS')); 

INSERT 
INTO users ( user_id, user_name, password, role, ssn, cell_phone, addr, email, reg_date ) 
VALUES ( 'manager', 'manager', '1234', 'admin', NULL, NULL, NULL, 'manager@mvc.com', to_date('2012/01/14 10:48:43', 'YYYY/MM/DD HH24:MI:SS'));          

INSERT INTO users 
VALUES ( 'user01', 'SCOTT', '1111', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user02', 'SCOTT', '2222', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user03', 'SCOTT', '3333', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user04', 'SCOTT', '4444', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user05', 'SCOTT', '5555', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user06', 'SCOTT', '6666', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user07', 'SCOTT', '7777', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user08', 'SCOTT', '8888', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user09', 'SCOTT', '9999', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user10', 'SCOTT', '1010', 'user', NULL, NULL, NULL, NULL, sysdate); 

INSERT INTO users 
VALUES ( 'user11', 'SCOTT', '1111', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user12', 'SCOTT', '1212', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user13', 'SCOTT', '1313', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user14', 'SCOTT', '1414', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user15', 'SCOTT', '1515', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user16', 'SCOTT', '1616', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user17', 'SCOTT', '1717', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user18', 'SCOTT', '1818', 'user', NULL, NULL, NULL, NULL, sysdate);

INSERT INTO users 
VALUES ( 'user19', 'SCOTT', '1919', 'user', NULL, NULL, NULL, NULL, sysdate);
           
           
insert into product values (seq_product_prod_no.nextval,'vaio vgn FS70B','소니 바이오 노트북 신동품','20120514',2000000, 'AHlbAAAAtBqyWAAA.jpg',to_date('2012/12/14 11:27:27', 'YYYY/MM/DD HH24:MI:SS'),'SALE');
insert into product values (seq_product_prod_no.nextval,'자전거','자전거 좋아요~','20120514',10000, 'AHlbAAAAvetFNwAA.jpg',to_date('2012/11/14 10:48:43', 'YYYY/MM/DD HH24:MI:SS'),'SALE');
insert into product values (seq_product_prod_no.nextval,'보르도','최고 디자인 신품','20120201',1170000, 'AHlbAAAAvewfegAB.jpg',to_date('2012/10/14 10:49:39', 'YYYY/MM/DD HH24:MI:SS'),'SALE');
insert into product values (seq_product_prod_no.nextval,'보드세트','한시즌 밖에 안썼습니다. 눈물을 머금고 내놓음 ㅠ.ㅠ','20120217', 200000, 'AHlbAAAAve1WwgAC.jpg',to_date('2012/11/14 10:50:58', 'YYYY/MM/DD HH24:MI:SS'),'SALE');
insert into product values (seq_product_prod_no.nextval,'인라인','좋아욥','20120819', 20000, 'AHlbAAAAve37LwAD.jpg',to_date('2012/11/14 10:51:40', 'YYYY/MM/DD HH24:MI:SS'),'SALE');
insert into product values (seq_product_prod_no.nextval,'삼성센스 2G','sens 메모리 2Giga','20121121',800000, 'AHlbAAAAtBqyWAAA.jpg',to_date('2012/11/14 18:46:58', 'YYYY/MM/DD HH24:MI:SS'),'SALE');
insert into product values (seq_product_prod_no.nextval,'연꽃','정원을 가꿔보세요','20121022',232300, 'AHlbAAAAtDPSiQAA.jpg',to_date('2012/11/15 17:39:01', 'YYYY/MM/DD HH24:MI:SS'),'SALE');
insert into product values (seq_product_prod_no.nextval,'삼성센스','노트북','20120212',600000, 'AHlbAAAAug1vsgAA.jpg',to_date('2012/11/12 13:04:31', 'YYYY/MM/DD HH24:MI:SS'),'SALE');

--여기부터 이서 DB--
insert into product values (seq_product_prod_no.nextval,'구미베어','곰돌이 모양 젤리','20250202',1800,'gummybear.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'초코도넛','초코시럽 듬뿍','20250216',3500,'choco_donut.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'레몬젤리','상큼터지는 레몬맛','20250104',1200,'lemon_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'카라멜팝콘','고소달콤 팝콘','20250218',2800,'caramel_popcorn.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'츄파춥스','추억의 막대사탕','20250207',1200,'chupa_chups.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'마카롱','알록달록 프렌치마카롱','20250126',3500,'macaron.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'유자사탕','상큼한 유자맛','20250120',1000,'yuzu_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'초코머핀','촉촉한 초코머핀','20250127',3000,'choco_muffin.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'수박젤리','여름한정 수박맛','20250106',1800,'watermelon_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'크런치바','바삭한 초코바','20250209',2200,'crunch_bar.jpg',sysdate,'SALE');

insert into product values (seq_product_prod_no.nextval,'민트사탕','시원한 민트향','20250114',800,'mint_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'초코쿠키','바삭한 초코칩 쿠키','20250121',2500,'choco_cookie.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'딸기머핀','달콤한 딸기머핀','20250129',3200,'strawberry_muffin.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'마쉬멜로우','폭신폭신 구름맛','20250201',2000,'marshmallow.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'블루베리젤리','블루베리향 가득','20250110',2000,'blueberry_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'크림롤케익','부드러운 크림롤','20250213',4200,'cream_rollcake.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'스키틀즈','무지개맛 사탕','20250204',2500,'skittles.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'카라멜사탕','고소한 카라멜맛','20250113',800,'caramel_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'블루베리머핀','상큼한 블루베리머핀','20250128',3200,'blueberry_muffin.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'딸기젤리','상큼한 딸기맛 쫄깃젤리','20250101',1500,'strawberry_jelly.jpg',sysdate,'SALE');

insert into product values (seq_product_prod_no.nextval,'머랭쿠키','사르르 녹는 머랭','20250214',2800,'meringue_cookie.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'허브사탕','목에 좋은 허브맛','20250116',1000,'herb_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'크림머핀','부드러운 크림 가득','20250130',3400,'cream_muffin.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'콜라젤리','톡쏘는 콜라맛','20250105',1000,'cola_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'치즈케익','부드러운 치즈케익','20250219',5000,'cheese_cake.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'라떼사탕','부드러운 라떼향','20250119',1200,'latte_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'딸기롤케익','상큼한 딸기롤','20250211',4000,'strawberry_rollcake.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'포도젤리','달콤한 포도향 가득','20250102',1500,'grape_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'롤리팝','알록달록 막대사탕','20250206',1500,'lollipop.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'초코롤케익','진한 초코롤','20250212',4200,'choco_rollcake.jpg',sysdate,'SALE');

insert into product values (seq_product_prod_no.nextval,'사과젤리','상큼한 사과맛','20250109',1500,'apple_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'버터쿠키','고소한 버터향 쿠키','20250122',2000,'butter_cookie.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'젤리롤','돌돌말린 젤리롤','20250210',2500,'jelly_roll.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'레몬쿠키','상큼한 레몬향 쿠키','20250125',2200,'lemon_cookie.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'체리젤리','달달한 체리맛','20250108',1500,'cherry_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'딸기쿠키','핑크빛 딸기쿠키','20250123',2200,'strawberry_cookie.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'무지개솜사탕','형형색색 예쁜 솜사탕','20250112',3000,'rainbow_cotton_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'에어헤드','쫀득쫀득 과일맛','20250205',2000,'airheads.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'망고젤리','열대과일 망고맛','20250107',2000,'mango_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'초코사탕','달콤한 초코볼','20250115',1200,'choco_candy.jpg',sysdate,'SALE');

insert into product values (seq_product_prod_no.nextval,'솜사탕','구름처럼 달콤한 솜사탕','20250111',2500,'cotton_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'유자사탕','상큼한 유자맛','20250120',1000,'yuzu_candy.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'블루베리머핀','상큼한 블루베리머핀','20250128',3200,'blueberry_muffin.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'젤리빈','알록달록 젤리빈','20250131',1500,'jellybean.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'크런치바','바삭한 초코바','20250209',2200,'crunch_bar.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'캔디케인','크리스마스 한정','20250208',2000,'candy_cane.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'초코도넛','초코시럽 듬뿍','20250216',3500,'choco_donut.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'오레오쿠키','달콤한 샌드쿠키','20250124',2300,'oreo_cookie.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'블루베리젤리','블루베리향 가득','20250110',2000,'blueberry_jelly.jpg',sysdate,'SALE');
insert into product values (seq_product_prod_no.nextval,'슈가도넛','달콤한 슈가도넛','20250217',3000,'sugar_donut.jpg',sysdate,'SALE');

commit;


//== Page 처리을 위한 SQL 구성연습

SELECT user_id , user_name , email
FROM users
ORDER BY user_id

currentPage =2
pageSize = 3   
4 ~ 6

SELECT inner_table. * ,  ROWNUM AS row_seq
FROM (	SELECT user_id , user_name , email
				FROM users
				ORDER BY user_id ) inner_table
WHERE ROWNUM <=6;	
//==>           currentPage * paseSize


SELECT * 
FROM (	SELECT inner_table. * ,  ROWNUM AS row_seq
				FROM (	SELECT user_id , user_name , email
								FROM users
								ORDER BY user_id ) inner_table
				WHERE ROWNUM <=6 )
WHERE row_seq BETWEEN 4 AND 6;

//==> (currentPage-1) * paseSize+1           currentPage * paseSize