package com.iloremipsumshowcase.blog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class IpsumPostController {
    private IpsumPostRepository ipsumPostRepo;
    private IpsumCategoryRepository ipsumCategoryRepo;
    private AuthorRepository authorRepo;
    private HashtagRepository hashtagRepo;
    public IpsumPostController(IpsumPostRepository ipsumPostRepo, IpsumCategoryRepository ipsumCategoryRepo, AuthorRepository authorRepo, HashtagRepository hashtagRepo) {
        this.ipsumPostRepo = ipsumPostRepo;
        this.ipsumCategoryRepo = ipsumCategoryRepo;
        this.authorRepo = authorRepo;
        this.hashtagRepo = hashtagRepo;
    }

    @GetMapping("ipsumposts/{ipsumName}")
    public String showSingleIpsumPost(@PathVariable String ipsumName, Model model){
        model.addAttribute("ipsumPostToDisplay",ipsumPostRepo.findByIpsumName(ipsumName));
        return "ipsumpost-template";
    }

    @PostMapping("ipsumposts/add")
    public String addIpsumPost(String ipsumName, String ipsumDescription, String ipsumSample, String ipsumSource, String date, String categoryName, String authorName, long... hashtagIds) {
        IpsumCategory postIpsumCategory = ipsumCategoryRepo.findByName(categoryName);
        Author postAuthor = authorRepo.findByName(authorName);
        Collection<Hashtag> postHashtags = Arrays.stream(hashtagIds)
                .mapToObj(id->hashtagRepo.findHashtagById(id))
                .collect(Collectors.toSet());
        ipsumPostRepo.save(new IpsumPost(ipsumName,ipsumDescription,
                ipsumSample, ipsumSource, date, postIpsumCategory,
                postAuthor, postHashtags.toArray(Hashtag[]::new)));

        return "redirect:/ipsumcategories/" +categoryName;
    }

    @PostMapping("ipsumposts/delete")
    public String deleteIpsumPost(long ipsumPostId){
       ipsumPostRepo.deleteById(ipsumPostId);
        return "redirect:/";
    }

}
