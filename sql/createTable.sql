CREATE TABLE transaction_tab(
	transaction_id	VARCHAR2(10)	CONSTRAINT transaction_tab_trid_pk	PRIMARY KEY,
	type 		VARCHAR2(10)	CONSTRAINT transaction_tab_type_nn	NOT NULL,
	amount		NUMBER(*,2)	CONSTRAINT transaction_tab_amt_nn	NOT NULL,
	status		VARCHAR2(10)	CONSTRAINT transaction_tab_status_nn	NOT NULL,
	tr_date		DATE		CONSTRAINT transaction_tab_date_nn	NOT NULL
);

CREATE TABLE  transaction_status_tab(
	transaction_status_id	VARCHAR2(10)	CONSTRAINT transaction_status_tab_trsts_id_pk	 	PRIMARY KEY,
	statusType		VARCHAR2(10)	CONSTRAINT transaction_status_tab_statusType_nn 	NOT NULL,
	reason			VARCHAR2(100)	CONSTRAINT transaction_status_reason_nn			NOT NULL,
	timestamp		DATE		CONSTRAINT transaction_status_date_nn			NOT NULL
);