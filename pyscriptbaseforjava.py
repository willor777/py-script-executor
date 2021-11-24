import sys
import json


class PyScriptBase:
    
    def load_kwargs_from_sys_argv(self) -> dict:
        """Loads keyword arguments from the Java Client Program. All kwargs are loaded as <String, String>.
        If there are None will return an empty dict"""

        base = sys.argv

        if len(base) < 1:
            return dict()

        base = base[-1]

        # Check if there are multiple kwargs. They are in format: "key:value|key:value"
        if "|" in base:
            base = base.split("|")
        else:
            base = [base]

        payload = dict()
        for kv in base:

            k,v = kv.split(":")

            payload[k] = v
        
        # Incase the .py side needs to know the Java Class that is running the script
        if "clientClassCode" in payload.keys():
            self.client_class_code = payload['clientClassCode']

        return payload

    @staticmethod
    def send_dict_as_json_output(self, *out_put_dicts):

        for d in out_put_dicts:

            jd = json.loads(d)
            print(jd)


    def main(self):

        # Example....
        # kwargs = self.load_kwargs_from_sys_argv()

        # Do program Stuff

        # payload = {"one": "1", "two": "2"}
        #
        # self.send_dict_as_json_output(payload)
        pass

if __name__ == '__main__':
    pass
