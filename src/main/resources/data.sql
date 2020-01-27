select * from book;

-- CREATE some authors
--INSERT INTO author(id, first_name, last_name, about) VALUES (1, 'Pavel', 'Dvořák', 'PhDr. Pavel Dvořák (* 13. máj 1937, Praha – † 21. december 2018)[1] bol slovenský historik, spisovateľ a publicista. Napísal viacero kníh a článkov v ktorých sa populárnym štýlom venoval hlavne slovenskej histórii.');
--INSERT INTO author(id, first_name, last_name, about) VALUES (2, 'Margaret', 'Cheney', 'Margaret Cheney (born 1955) is an American mathematician whose research involves inverse problems.She is Yates Chair and Professor of Mathematics at Colorado State University.');
--INSERT INTO author(id, first_name, last_name, about) VALUES (3, 'Kitty', 'Fergusonová', 'Kitty Gail Ferguson (née Vetter) (born December 16, 1941)is an American science writer, lecturer, and former professional musician.');
--INSERT INTO author(id, first_name, middle_name, last_name, about) VALUES (4, 'Pavel', 'Hirax', 'Baričák', 'Martinský spisovateľ, poet, hudobník, bloger, filozof, cestovateľ a fotograf, ktorý na svojich čítačkách okrem prezentovania básní a románov veľmi rád vedie filozofické debaty, ako aj rozpráva o pozitívnom myslení.');

-- CREATE some publishers
--INSERT INTO publisher(id, name, address, web_address) VALUES (1, 'Rak', 'Budmerice 819, 90086, Slovensko', 'https://vydavatelstvorak.sk');
--INSERT INTO publisher(id, name, address, web_address) VALUES (2, 'Citadela', 'Karloveská 416/49, 841 04  Bratislava, Slovensko', 'http://www.citadella.sk');
--INSERT INTO publisher(id, name, address, web_address) VALUES (3, 'Slovenský spisovateľ', 'Miletičova 23, Bratislava 821 09, Slovensko', 'https://slovenskyspisovatel.sk');
--INSERT INTO publisher(id, name, address, web_address) VALUES (4, 'HladoHlas', 'Rumunskej armády 1/6, 036 01 Martin, Slovensko', 'http://hladohlas.sk');

-- CREATE some books
--INSERT INTO book(id, isbn10, title, author_id, publisher_id) VALUES (1, '8085501228', 'Stopy dávnej minulosti 1 (Slovensko v praveku)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (2, '9788085501490', 'Stopy dávnej minulosti 2 (Slovensko v staroveku)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (3, '9788085501599', 'Stopy dávnej minulosti 3 (Zrod národa)', 1, 1); 
--INSERT INTO book(id, isbn10, title, author_id, publisher_id) VALUES (4, '8085510295', 'Stopy dávnej minulosti 4 (Slovensko v Uhorskom kráľovstve)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (5, '9788085501438', 'Stopy dávnej minulosti 5 (Slovensko v stredoveku)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (6, '9788085501582', 'Stopy dávnej minulosti 6 (Slovensko v stredoveku. Čas cudzích kráľov)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (7, '9788085501612', 'Stopy dávnej minulosti 7 (Slovensko na konci stredoveku)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (8, '9788085501650', 'Stopy dávnej minulosti 8 (Slovensko v čase tureckých vojen - Smrť sultána Sulejmana)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (9, '9788085501704', 'Stopy dávnej minulosti 9 - Posledný Turek (Stavovské povstania a koniec osmanských vojen)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (10, '9788085501735', 'Stopy dávnej minulosti 10 (O láske, práci a histórii)', 1, 1);
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (11, '9788097087579', 'Tesla - člověk mimo čas', 2, 2); 
--INSERT INTO book(id, isbn10, title, author_id, publisher_id) VALUES (12, '8022008486', 'Žaláre svetla', 3, 3); 
--INSERT INTO book(id, isbn13, title, author_id, publisher_id) VALUES (13, '9788089711390', 'Indonézia', 4, 4); 


-- CREATE some pictures
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (1, 'image/jpeg', 'cov1.jpg', 1, 0, LOAD_FILE('/tmp/cov1.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (2, 'image/jpeg', 'cov2.jpg', 1, 0, LOAD_FILE('/tmp/cov2.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (3, 'image/jpeg', 'cov3.jpg', 1, 0, LOAD_FILE('/tmp/cov3.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (4, 'image/jpeg', 'cov4.jpg', 1, 0, LOAD_FILE('/tmp/cov4.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (5, 'image/jpeg', 'cov5.jpg', 1, 0, LOAD_FILE('/tmp/cov5.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (6, 'image/jpeg', 'cov6.jpg', 1, 0, LOAD_FILE('/tmp/cov6.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (7, 'image/jpeg', 'cov7.jpg', 1, 0, LOAD_FILE('/tmp/cov7.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (8, 'image/jpeg', 'cov8.jpg', 1, 0, LOAD_FILE('/tmp/cov8.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (9, 'image/jpeg', 'cov9.jpg', 1, 0, LOAD_FILE('/tmp/cov9.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (10, 'image/jpeg', 'cov10.jpg', 1, 0, LOAD_FILE('/tmp/cov10.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (11, 'image/jpeg', 'cov11.jpg', 1, 0, LOAD_FILE('/tmp/cov11.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (12, 'image/jpeg', 'cov12.jpg', 1, 0, LOAD_FILE('/tmp/cov12.jpg'));
--INSERT INTO picture(id, mimetype, name, book_id, picture_type, picture) VALUES (13, 'image/jpeg', 'cov13.jpg', 1, 0, LOAD_FILE('/tmp/cov13.jpg'));
