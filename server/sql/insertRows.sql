-- Transactions Table
TRUNCATE TABLE transaction_tab;

INSERT INTO transaction_tab VALUES('1', 'upi', '22.50', 'initiated', '23-Apr-2024');
INSERT INTO transaction_tab VALUES('2', 'bank', '2000.00', 'completed', '12-Apr-2024');
INSERT INTO transaction_tab VALUES('3', 'bank', '3000.50', 'pending', '18-Apr-2024');
INSERT INTO transaction_tab VALUES('4', 'upi', '22.50', 'completed', '23-Apr-2024');
INSERT INTO transaction_tab VALUES('5', 'bank', '22.50', 'completed', '03-July-2024');
INSERT INTO transaction_tab VALUES('6', 'bank', '2400.50', 'pending', '23-Apr-2024');
INSERT INTO transaction_tab VALUES('7', 'upi', '22.50', 'completed', '21-May-2024');
INSERT INTO transaction_tab VALUES('8', 'bank', '10000.00', 'failed', '30-Apr-2024');
INSERT INTO transaction_tab VALUES('9', 'bank', '220.50', 'completed', '19-Jan-2024');
INSERT INTO transaction_tab VALUES('10', 'bank', '300.75', 'completed', '23-Apr-2024');

SELECT 
	transaction_id AS id,
	type,
	amount,
	status,
	transaction_date as tr_date
FROM transaction_tab;

-- Transaction Status Table
TRUNCATE TABLE transaction_status_tab;

INSERT INTO transaction_status_tab VALUES('1', 'completed', 'Money received by recepient', '19-Jun-2024');
INSERT INTO transaction_status_tab VALUES('2', 'pending', 'Money debited from sender', '19-Jul-2024');
INSERT INTO transaction_status_tab VALUES('3', 'initiated', 'Approve payment request', '19-Apr-2024');
INSERT INTO transaction_status_tab VALUES('4', 'failed', 'Transaction aborted.', '19-Jun-2024');
INSERT INTO transaction_status_tab VALUES('5', 'completed', 'Money received by recepient', '19-May-2024');
INSERT INTO transaction_status_tab VALUES('6', 'pending', 'Money debited from sender', '19-Feb-2024');
INSERT INTO transaction_status_tab VALUES('7', 'initiated', 'Approve payment request', '19-Aug-2024');
INSERT INTO transaction_status_tab VALUES('8', 'failed', 'Transaction aborted.', '19-Nov-2024');

SELECT
	transaction_status_id AS id,
	statustype AS type,
	reason AS message,
	timestamp AS tr_status_dt
FROM transaction_status_tab;