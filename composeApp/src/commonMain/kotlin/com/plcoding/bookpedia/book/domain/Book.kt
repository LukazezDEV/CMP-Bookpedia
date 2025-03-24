package com.plcoding.bookpedia.book.domain

import kotlin.random.Random

data class Book(
    val id: String,
    val title: String,
    val imageUrl: String,
    val authors: List<String>,
    val description: String?,
    val languages: List<String>,
    val firstPublishYear: Int?,
    val genres: List<String>,
    val averageRating: Double?,
    val numRatings: Int?,
    val numPages: Int?,
    val numEditions: Int?
)

object BookMockAttributes{
    fun randomGenres(maxAmount: Int = 4) = genres.shuffled().take(Random.nextInt(1, maxAmount))
    private val genres = setOf(
    "Fantasy",
    "Science Fiction",
    "Mystery",
    "Thriller",
    "Romance",
    "Historical Fiction",
    "Horror",
    "Adventure",
    "Non-Fiction",
    "Biography",
    "Self-Help",
    "Poetry",
    "Young Adult",
    "Graphic Novel",
    "Dystopian",
    "Contemporary",
    "Children's Literature",
    "Memoir",
    "Cookbook",
    "True Crime"
    )

    fun randomLanguages(maxAmount: Int = 6) = languages.shuffled().take(Random.nextInt(1, maxAmount))
    val languages = listOf(
        "English",
        "Mandarin Chinese",
        "Hindi",
        "Spanish",
        "French",
        "Arabic",
        "Bengali",
        "Russian",
        "Portuguese",
        "Urdu",
        "Indonesian",
        "German",
        "Japanese",
        "Nigerian Pidgin",
        "Marathi",
        "Telugu",
        "Turkish",
        "Tamil",
        "Yue Chinese (Cantonese)",
        "Vietnamese"
    )

    fun randomAuthors(maxAmount: Int = 3) = authors.shuffled().take(Random.nextInt(1, maxAmount))
    val authors = listOf(
        "J.K. Rowling",
        "Stephen King",
        "George R.R. Martin",
        "J.R.R. Tolkien",
        "Agatha Christie",
        "Jane Austen",
        "Suzanne Collins",
        "Harper Lee",
        "F. Scott Fitzgerald",
        "Markus Zusak"
    )

    val books = listOf(
        "Harry Potter and the Sorcerer's Stone",
        "The Hunger Games",
        "To Kill a Mockingbird",
        "The Hobbit",
        "1984",
        "Pride and Prejudice",
        "The Book Thief",
        "The Fellowship of the Ring",
        "The Catcher in the Rye",
        "The Great Gatsby",
        "Divergent",
        "The Fault in Our Stars",
        "The Girl on the Train",
        "The Help",
        "The Night Circus",
        "The Maze Runner",
        "The Giver",
        "The Alchemist",
        "The Lightning Thief",
        "The Perks of Being a Wallflower"
    )

    val urls = listOf(
        "https://picsum.photos/200/300",
        "https://source.unsplash.com/random/200x300",
        "https://placekitten.com/200/300",
        "https://placebear.com/200/300",
        "https://loremflickr.com/200/300",
        "https://dummyimage.com/200x300",
        "https://robohash.org/random.png?size=200x300",
        "https://www.fillmurray.com/200/300",
        "https://baconmockup.com/200/300",
        "https://placeimg.com/200/300/any",
        "https://picsum.photos/id/10/200/300",
        "https://placeimg.com/200/300/animals",
        "https://www.fillmurray.com/300/200",
        "https://loremflickr.com/200/300/nature",
        "https://picsum.photos/200/300/?blur",
        "https://picsum.photos/200/300?grayscale",
        "https://placekitten.com/400/400",
        "https://placebear.com/300/400",
        "https://unsplash.it/200/300",
        "https://www.placecage.com/200/300"
    )
}