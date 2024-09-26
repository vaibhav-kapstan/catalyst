import time
import logging
import random
import traceback
from datetime import datetime

# Configure logging with consistent timestamp format
logging.basicConfig(
    level=logging.ERROR,
    format='%(asctime)s %(levelname)s %(message)s',
    datefmt='%Y-%m-%dT%H:%M:%S%z'
)

def emit_error():
    error_types = ['ZeroDivisionError', 'AttributeError', 'ValueError', 'TypeError']
    error = random.choice(error_types)
    
    try:
        if error == 'ZeroDivisionError':
            1 / 0
        elif error == 'AttributeError':
            None.some_attribute
        elif error == 'ValueError':
            int("invalid")
        elif error == 'TypeError':
            len(5)
    except Exception as e:
        logging.error("An error occurred: %s\n%s", e, traceback.format_exc())

def main():
    while True:
        emit_error()
        time.sleep(60)

if __name__ == "__main__":
    main()