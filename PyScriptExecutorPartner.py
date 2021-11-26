import sys


class PyScriptExecutorHelper:

    def __init__(self):
        # Incase the py file needs to know which java class is calling it
        self.client_class_code = ""
        self.java_kwargs = self.load_kwargs_from_sys_argv()

    def load_kwargs_from_sys_argv(self) -> dict:
        """Loads keyword arguments from the Java Client Program.
        If there are None will return an empty dict"""

        base = sys.argv
        if len(base) < 1:
            return dict()

        base = base[-1]
        # Check if there are multiple kwargs. They are in format: "key:value|key:value"
        if "$$" in base:
            base = base.split("$$")
        else:
            base = [base]

        payload = dict()
        for kv in base:
            if kv == "" or ":" not in kv:
                continue
            k, v = kv.split(":")

            payload[k] = v

        if "clientClassCode" in payload.keys():
            self.client_class_code = payload['clientClassCode']

        return payload

    @staticmethod
    def print_dict_as_json(*dict_to_json: dict):
        """
        Use to return Json strings to Java Client Class to be processed into HashMap<String, String>.
        All values should be Java Compatable
        """
        for d in dict_to_json:
            print(json.dumps(d))

