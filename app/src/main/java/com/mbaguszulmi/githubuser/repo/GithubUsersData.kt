package com.mbaguszulmi.githubuser.repo

import com.mbaguszulmi.githubuser.model.GithubUser

object GithubUsersData {
    private val names = arrayOf(
        "Muhammad Bagus Zulmi",
        "Fabien Potencier",
        "Andrew Nesbitt",
        "Taylor Otwell",
        "EGOIST",
        "Hugo Giraudel",
        "Thibault Duplessis",
        "Juho Vepsäläinen",
        "Nelson",
        "Alex Crichton"
    )

    private val usernames = arrayOf(
        "mbaguszulmi",
        "fabpot",
        "andrew",
        "taylorotwell",
        "egoist",
        "HugoGiraudel",
        "ornicar",
        "bebraw",
        "nelsonic",
        "alexcrichton"
    )

    private val repoCounts = arrayOf(
        37,
        46,
        266,
        15,
        705,
        38,
        371,
        170,
        298,
        404
    )

    private val followers = arrayOf(
        4,
        10700,
        2400,
        19300,
        7500,
        2500,
        2500,
        1800,
        2900,
        3100
    )
    private val followings = arrayOf(
        2,
        0,
        3100,
        0,
        26,
        0,
        169,
        0,
        17700,
        0
    )

    private val avatarUrls = arrayOf(
        "https://avatars0.githubusercontent.com/u/28475291?s=460&u=46a8378fe5ca57ab8ddc65aab57d98b4b35bda65&v=4",
        "https://avatars3.githubusercontent.com/u/47313?s=460&u=7ba05204271a726f8642ac15864e2f361b5c0198&v=4",
        "https://avatars3.githubusercontent.com/u/1060?s=400&u=d4790e0ec60657f07aae1a398d7171f167b8d8d0&v=4",
        "https://avatars3.githubusercontent.com/u/463230?s=400&u=0c486fbe3a30dadd5c5981a9fbc3a0d269ca0c33&v=4",
        "https://avatars1.githubusercontent.com/u/8784712?s=400&u=f85088111cb1fa9d1cb770278dd54ed8a2e49f09&v=4",
        "https://avatars1.githubusercontent.com/u/1889710?s=400&u=2461550e874dca87baa3425ee2efd1b8179e207b&v=4",
        "https://avatars3.githubusercontent.com/u/140370?s=400&v=4",
        "https://avatars0.githubusercontent.com/u/166921?s=400&u=01788cd9ae25b31e3c0f2dc5b8c79631e14a59ba&v=4",
        "https://avatars3.githubusercontent.com/u/194400?s=400&u=405fb897c18f414ea8c4f4f2613fbef605855f02&v=4",
        "https://avatars0.githubusercontent.com/u/64996?s=400&v=4"
    )

    val githubUsers: ArrayList<GithubUser> get() {
        val list = ArrayList<GithubUser>();
        for (position in names.indices) {
            val user = GithubUser(names[position], usernames[position], repoCounts[position],
                followers[position], followings[position], "Lorem ipsum dolor sit amet", avatarUrls[position])
            list.add(user)
        }
        return list
    }
}