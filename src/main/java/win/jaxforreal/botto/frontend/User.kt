package win.jaxforreal.botto.frontend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.backend.Privilege

data class User(val name: String, val trip: String, val frontend: Frontend) {

    fun getPrivilegeLevel(): Privilege {
        for (privilegeName in privilegeNames) {
            //get the list of users of each privilege type
            //print(privilegeName)
            val userTable = Config.get<List<Map<String, Any>>>("privilege", privilegeName)
            //for each user on list, check if it matches this user
            for (userInfoMap in userTable) {
                if (userInfoMap["name"] == name &&
                        userInfoMap["trip"] == trip &&
                        userInfoMap["frontend"] == frontend.name) {
                    //if match, return the level
                    return Privilege.valueOf(privilegeName)
                }
            }
        }

        return Privilege.USER
    }

    companion object {
        //drop the USER level, because it is default and doesn't need to be tested
        val privilegeNames = Privilege.values().map { it.name }.drop(1)
    }
}