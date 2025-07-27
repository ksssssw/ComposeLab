package com.ksssssw.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ksssssw.spacex.model.Dimension
import com.ksssssw.spacex.model.Mass
import com.ksssssw.spacex.model.Rocket

class RocketsPreviewParameterProvider : PreviewParameterProvider<List<Rocket>> {
    override val values: Sequence<List<Rocket>>
        get() = sequenceOf(
            listOf(
                Rocket(
                    id = "5e9d0d95eda69955f709d1eb",
                    name = "Falcon 1",
                    description = "The Falcon 1 was an expendable launch system privately developed and manufactured by SpaceX during 2006-2009. On 28 September 2008, Falcon 1 became the first privately-developed liquid-fuel launch vehicle to go into orbit around the Earth.",
                    active = false,
                    stages = 2,
                    boosters = 0,
                    costPerLaunch = 6700000,
                    successRatePct = 40,
                    firstFlight = "2006-03-24",
                    country = "Republic of the Marshall Islands",
                    company = "SpaceX",
                    height = Dimension(120.0, 100.0),
                    diameter = Dimension(120.0, 100.0),
                    mass = Mass(12345, 12345),
                    flickrImages = listOf(
                        "https://imgur.com/DaCfMsj.jpg",
                        "https://imgur.com/azYafd8.jpg"
                    )
                ),
                Rocket(
                    id = "5e9d0d95eda69973a809d1ec",
                    name = "Falcon 9",
                    description = "Falcon 9 is a two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of satellites and the Dragon spacecraft into orbit.",
                    active = true,
                    stages = 2,
                    boosters = 0,
                    costPerLaunch = 50000000,
                    successRatePct = 98,
                    firstFlight = "2010-06-04",
                    country = "United States",
                    company = "SpaceX",
                    height = Dimension(120.0, 100.0),
                    diameter = Dimension(120.0, 100.0),
                    mass = Mass(12345, 12345),
                    flickrImages = listOf(
                        "https://farm1.staticflickr.com/929/28787338307_3453a11a77_b.jpg",
                        "https://farm4.staticflickr.com/3955/32915197674_eee74d81bb_b.jpg",
                        "https://farm1.staticflickr.com/293/32312415025_6841e30bf1_b.jpg",
                        "https://farm1.staticflickr.com/623/23660653516_5b6cb301d1_b.jpg",
                        "https://farm6.staticflickr.com/5518/31579784413_d853331601_b.jpg",
                        "https://farm1.staticflickr.com/745/32394687645_a9c54a34ef_b.jpg"
                    )
                ),
                Rocket(
                    id = "5e9d0d95eda69974db09d1ed",
                    name = "Falcon Heavy",
                    description = "With the ability to lift into orbit over 54 metric tons (119,000 lb)--a mass equivalent to a 737 jetliner loaded with passengers, crew, luggage and fuel--Falcon Heavy can lift more than twice the payload of the next closest operational vehicle, the Delta IV Heavy, at one-third the cost.",
                    active = true,
                    stages = 2,
                    boosters = 2,
                    costPerLaunch = 90000000,
                    successRatePct = 100,
                    firstFlight = "2018-02-06",
                    country = "United States",
                    company = "SpaceX",
                    height = Dimension(120.0, 100.0),
                    diameter = Dimension(120.0, 100.0),
                    mass = Mass(12345, 12345),
                    flickrImages = listOf(
                        "https://farm5.staticflickr.com/4599/38583829295_581f34dd84_b.jpg",
                        "https://farm5.staticflickr.com/4645/38583830575_3f0f7215e6_b.jpg",
                        "https://farm5.staticflickr.com/4696/40126460511_b15bf84c85_b.jpg",
                        "https://farm5.staticflickr.com/4711/40126461411_aabc643fd8_b.jpg"
                    )
                ),
                Rocket(
                    id = "5e9d0d96eda699382d09d1ee",
                    name = "Starship",
                    description = "Starship and Super Heavy Rocket represent a fully reusable transportation system designed to service all Earth orbit needs as well as the Moon and Mars. This two-stage vehicle — composed of the Super Heavy rocket (booster) and Starship (ship) — will eventually replace Falcon 9, Falcon Heavy and Dragon.",
                    active = false,
                    stages = 2,
                    boosters = 0,
                    costPerLaunch = 7000000,
                    successRatePct = 0,
                    firstFlight = "2021-12-01",
                    country = "United States",
                    company = "SpaceX",
                    height = Dimension(120.0, 100.0),
                    diameter = Dimension(120.0, 100.0),
                    mass = Mass(12345, 12345),
                    flickrImages = listOf(
                        "https://live.staticflickr.com/65535/48954138962_ee541e6755_b.jpg",
                        "https://live.staticflickr.com/65535/48953946911_e60c5bcc5c_b.jpg",
                        "https://live.staticflickr.com/65535/48954138922_9c42173f08_b.jpg",
                        "https://live.staticflickr.com/65535/48953947006_313f01ec93_b.jpg"
                    )
                )
            )
        )
}